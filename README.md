NLPAnnotator
============

This package annotates dates from plain text.

Build & Run
-----------

- `gradle build` (will complain about missing tests, don't worry)
- `gradle -Pport=3030 execute` where 3030 is the server port
- `echo "First Tuesday of May or next Monday" | python client_socket.py`
