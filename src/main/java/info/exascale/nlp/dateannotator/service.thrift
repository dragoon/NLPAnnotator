namespace * com.farawaytech.nlp.dateannotator.thrift

struct TTimeAnnotation {
	1: required string startToken
	2: required string endToken
	3: required string temporal
}


struct TAnnotationResponse {
	1: required list<TTimeAnnotation> annotations
}


service DateAnnotatorService {

	/** Extracts date annotations from a given sentence/string.
	* The sentence has to be already properly tokenized, only whitespace split will be applied.
	* Date argument is required for relative dates, otheriwise current date will be applied.
	*/
	TAnnotationResponse annotate(1:string sentence, 2:string date),

	/**
	* Performs inline datetime annotations on a given sentence.
	**/
	string annotateInline(1:string sentence, 2:string date),
}
