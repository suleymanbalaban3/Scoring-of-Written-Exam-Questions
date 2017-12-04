package Sentence;

import java.util.ArrayList;
import java.util.List;

import WordEmbedding.Word;

public class Sentence{
	private List<Word>wordOfSentence;
	private List<Double>averageWordVector;
	private String sentence;
	
	public List<Word> getWordOfSentence() {
		return wordOfSentence;
	}
	public void setWordOfSentence(List<Word> wordOfSentence) {
		this.wordOfSentence = wordOfSentence;
	}
	public Sentence() {
	}
	public Sentence(String sentence) {
		this.setSentence(sentence);
		wordOfSentence = new ArrayList<>();
		String [] line = sentence.split(" ");
		for (int i = 0; i < line.length; i++) {
			wordOfSentence.add(new Word(line[i]));
		}
}
	/**
	 * @return the sentence
	 */
	public String getSentence() {
		return sentence;
	}
	/**
	 * @param sentence the sentence to set
	 */
	public void setSentence(String sentence) {
		this.sentence = sentence;
	}
	/**
	 * @return the averageWordVector
	 */
	public List<Double> getAverageWordVector() {
		return averageWordVector;
	}
	/**
	 * @param averageWordVector the averageWordVector to set
	 */
	public void setAverageWordVector(List<Double> averageWordVector) {
		this.averageWordVector = averageWordVector;
	}
	public void genereteAverageVector() {
		averageWordVector = new ArrayList<>(200);

		for (int i = 0; i < 200; i++) 
			getAverageWordVector().add(0.0);
			
		for (int i = 0; i < getWordOfSentence().size(); i++) {
			for (int j = 0; j < getWordOfSentence().get(i).getWordDenseVector().size(); j++) {
				getAverageWordVector().set(j, getAverageWordVector().get(j) + (getWordOfSentence().get(i).getWordDenseVector().get(j) / getWordOfSentence().size()));
			}
			
		}
	}
}
