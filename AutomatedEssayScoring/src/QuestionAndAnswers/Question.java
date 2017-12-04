package QuestionAndAnswers;

import Sentence.Sentence;

public class Question extends Sentence{
	
	public Question() {

	}
	public Question(String question) {
		super(question);
	}
	@Override
	public String toString() {
		return "Question :" + getSentence();
	}
}
