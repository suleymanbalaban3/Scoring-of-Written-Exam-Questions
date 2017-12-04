package WordEmbedding;

import java.util.List;

import javax.jws.Oneway;

public class Word {
	private String word;
	private List<Double> wordDenseVector;
	
	public Word() {
		// TODO Auto-generated constructor stub
	}
	public Word(String word) {
		this.setWord(word);
	}
	public Word(String word, List<Double> wordDenseVector) {
		this.setWord(word);
		this.setWordDenseVector(wordDenseVector);
	}
	/**
	 * @return the word
	 */
	public String getWord() {
		return word;
	}
	/**
	 * @param word the word to set
	 */
	public void setWord(String word) {
		this.word = word;
	}
	/**
	 * @return the wordDenseVector
	 */
	public List<Double> getWordDenseVector() {
		return wordDenseVector;
	}
	/**
	 * @param wordDenseVector the wordDenseVector to set
	 */
	public void setWordDenseVector(List<Double> wordDenseVector) {
		this.wordDenseVector = wordDenseVector;
	}
	@Override
	public String toString() {
		String result = getWord();
		for (int i = 0; i < getWordDenseVector().size(); i++) {
			result += getWordDenseVector().get(i) + "  ";
		}
		return result;
	}
}