package com.farawaytech.nlp.corefannotator;

import edu.stanford.nlp.classify.LogisticClassifier;
import edu.stanford.nlp.dcoref.*;
import edu.stanford.nlp.pipeline.DefaultPaths;
import edu.stanford.nlp.util.StringUtils;
import edu.stanford.nlp.util.logging.NewlineLogFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;


public class IntermediateCorefSystem extends SieveCoreferenceSystem {
    public IntermediateCorefSystem(Properties props) throws Exception {
        super(props);
        List<String> lines = new ArrayList<>();
        // header
        StringJoiner header = new StringJoiner("\t");
        header.add("DOC_ID");
        header.add("PARAGRAPH_ID");
        header.add("SENT_ID");
        header.add("START_INDEX");
        header.add("END_INDEX");
        header.add("MENTION");
        header.add("NER_ENTITY");
        header.add("HEAD_WORD_LEMMA");
        header.add("HEAD_POS_TAG");
        header.add("COREF_ID");
        header.add("W2V_TYPES");
        lines.add(header.toString());
        try {
            Files.write(Paths.get("corefs.txt"), lines, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Map<Integer, CorefChain> coref(Document document) throws Exception {
        Map<Integer, CorefChain> corefChains = super.coref(document);
        List<String> lines = new ArrayList<>();
        List<List<Mention>> orderedMentions = filterMentionsWithSingletonClusters(document, document.predictedOrderedMentionsBySentence);
        for (List<Mention> mentionCluster: orderedMentions) {

            for (Mention mention : mentionCluster) {
                String posHeadTag = "NULL";
                String lemma;
                try {
                    lemma = mention.headIndexedWord.lemma();
                }
                catch (NullPointerException e) {
                    lemma = mention.headString;
                }

                try {
                    posHeadTag = mention.headIndexedWord.toString().split("/")[1];
                } catch (NullPointerException ignored) {
                }

                String resolvedTypes = "NULL";
                if (posHeadTag.startsWith("NN") && mention.nerString.equals("O") && !lemma.isEmpty())
                    try {
                        resolvedTypes = String.join(",", getTypesForNgram(lemma));
                    }
                    catch (Exception ignored) {}

                StringJoiner line = new StringJoiner("\t");
                line.add(document.conllDoc.getDocumentID());
                line.add(document.conllDoc.getPartNo());
                line.add(String.valueOf(mention.sentNum));
                line.add(String.valueOf(mention.startIndex));
                line.add(String.valueOf(mention.endIndex));
                line.add(mention.spanToString());
                line.add(mention.nerString);
                line.add(lemma);
                line.add(posHeadTag);
                line.add(String.valueOf(mention.corefClusterID));
                line.add(resolvedTypes);
                lines.add(line.toString());
            }
        }
        try {
            Files.write(Paths.get("corefs.txt"), lines, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return corefChains;
    }

    /**
     * Returns the name of the log file that this method writes.
     */
    public static String initializeAndRunCoref(Properties props) throws Exception {
        String timeStamp = Calendar.getInstance().getTime().toString().replaceAll("\\s", "-").replaceAll(":", "-");
        LogisticClassifier<String, String> singletonPredictor = getSingletonPredictorFromSerializedFile(props.getProperty(Constants.SINGLETON_MODEL_PROP, DefaultPaths.DEFAULT_DCOREF_SINGLETON_MODEL));

        //
        // initialize logger
        //
        String logFileName = props.getProperty(Constants.LOG_PROP, "log.txt");
        if (logFileName.endsWith(".txt")) {
            logFileName = logFileName.substring(0, logFileName.length() - 4) + "_" + timeStamp + ".txt";
        } else {
            logFileName = logFileName + "_" + timeStamp + ".txt";
        }
        try {
            FileHandler fh = new FileHandler(logFileName, false);
            logger.addHandler(fh);
            logger.setLevel(Level.FINE);
            fh.setFormatter(new NewlineLogFormatter());
        } catch (SecurityException | IOException e) {
            System.err.println("ERROR: cannot initialize logger!");
            throw e;
        }

        // initialize coref system
        SieveCoreferenceSystem corefSystem = new IntermediateCorefSystem(props);

        MentionExtractor mentionExtractor = null;
        if (props.containsKey(Constants.CONLL2011_PROP)) {
            mentionExtractor = new CoNLLMentionExtractor(corefSystem.dictionaries(), props,
                    corefSystem.semantics(), singletonPredictor);
        }
        if (mentionExtractor == null) {
            throw new RuntimeException("No input file specified!");
        }
        // Set mention finder
        String mentionFinderClass = props.getProperty(Constants.MENTION_FINDER_PROP);
        if (mentionFinderClass != null) {
            String mentionFinderPropFilename = props.getProperty(Constants.MENTION_FINDER_PROPFILE_PROP);
            CorefMentionFinder mentionFinder;
            if (mentionFinderPropFilename != null) {
                Properties mentionFinderProps = new Properties();
                FileInputStream fis = new FileInputStream(mentionFinderPropFilename);
                mentionFinderProps.load(fis);
                fis.close();
                mentionFinder = (CorefMentionFinder) Class.forName(mentionFinderClass).getConstructor(Properties.class).newInstance(mentionFinderProps);
            } else {
                mentionFinder = (CorefMentionFinder) Class.forName(mentionFinderClass).newInstance();
            }
            mentionExtractor.setMentionFinder(mentionFinder);
        }
        if (mentionExtractor.mentionFinder == null) {
            logger.warning("No mention finder specified, but not using gold mentions");
        }

        try {
            runAndScoreCoref(corefSystem, mentionExtractor, props, timeStamp);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "ERROR in running coreference", ex);
        }
        return logFileName;
    }

    public static List<String> getTypesForNgram(String ngram) throws IOException, JSONException {
        List<String> types = new ArrayList<>();
        URL url = new URL("http://diufpc54:5000/predict/types/word?ngram=" + ngram);
        Scanner scan = new Scanner(url.openStream());
        String str = "";
        while (scan.hasNext())
            str += scan.nextLine();
        scan.close();

        JSONObject obj = new JSONObject(str);
        JSONArray results = obj.getJSONArray("types");
        for (int i=0; i<results.length(); i++) {
            String type = results.getJSONArray(i).getString(0);
            String similarity = results.getJSONArray(i).getString(1);
            types.add(type);
        }
        return types;
    }

    public static void main(String[] args) throws Exception {
        Properties props = StringUtils.argsToProperties(args);
        initializeAndRunCoref(props);
    }
}
