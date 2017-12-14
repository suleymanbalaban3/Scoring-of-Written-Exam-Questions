package WordEmbedding;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.annotation.Retention;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;

import QuestionAndAnswers.Answer;
import QuestionAndAnswers.DataSet;


public class Embedding {
	private String fileName;
	private int DimensionSize;
	public int getDimensionSize() {
		return DimensionSize;
	}
	public void setDimensionSize(int dimensionSize) {
		DimensionSize = dimensionSize;
	}
	public Embedding() {
		
	}
	public Embedding(String fileName) {
		this.setFileName(fileName);
		this.setDimensionSize(200);
	}
	public Embedding(String fileName, String word2VecOrFastText) {
		this.setFileName(fileName);
		if(word2VecOrFastText == "Word2Vec") {
			this.setDimensionSize(200);
		}else if(word2VecOrFastText == "FastText") {
			this.setDimensionSize(100);
		}else {
			this.setDimensionSize(200);
		}
	}
	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public List<Answer> vectorsFromCorpora(List<Answer>answerData){
		int wordNum = 0;
	    int denseSize = 0;
	    int foundedWordVectorCount = 0;
	    String line;
	    List<Double>avgDenseVector = new ArrayList<>(getDimensionSize());
	    List<Double>avgDenseVectorTemp = new ArrayList<>(getDimensionSize());
	    HashMap<String, List<Double>>denseVector = new HashMap<>();
	    
	    for (int i = 0; i < answerData.size(); i++) {
			for (int j = 0; j < answerData.get(i).getWordOfSentence().size(); j++) {
				denseVector.put(answerData.get(i).getWordOfSentence().get(j).getWord(), new ArrayList<Double>());
			}
		}
	    
	    loadZeroToInitialDensevector(avgDenseVector);
	    loadZeroToInitialDensevector(avgDenseVectorTemp);
		try {
			FileInputStream file = new FileInputStream(getFileName());
            @SuppressWarnings("resource")
			Scanner scanner = new Scanner(file,"UTF-8");
            wordNum = scanner.nextInt();
            denseSize = scanner.nextInt();
            scanner.nextLine();

            for (int i = 0; i < wordNum; i++) {
				line = scanner.next();
				//System.out.println(line + " /  i: " + i);
				if(denseVector.containsKey(line)) {
					List<Double> readedOneDenseVector = new ArrayList<>(getDimensionSize());
					for (int j = 0; j < denseSize; j++) {
						readedOneDenseVector.add(Double.parseDouble(scanner.next()));
					}
					for (int j = 0; j < denseSize; j++) {
						avgDenseVector.set(j, avgDenseVector.get(j) + readedOneDenseVector.get(j));
					}
					denseVector.put(line, readedOneDenseVector);
					foundedWordVectorCount++;
				}else {
					String junk = scanner.nextLine();
					String [] args = junk.split(" ");
					for (int j = 1; j < args.length; j++) {
						avgDenseVector.set(j-1, avgDenseVector.get(j-1) + Double.parseDouble(args[j]));
					}
				}
				if(foundedWordVectorCount == denseVector.size())
					break;
			} 
        } catch (FileNotFoundException e) {  
        	System.err.println("Vector's file not found!");
            e.printStackTrace();
        }
		for (int i = 0; i < denseSize; i++) {
			avgDenseVector.set(i, avgDenseVector.get(i) / wordNum );
		}
		for (int i = 0; i < answerData.size(); i++) {
			for (int j = 0; j < answerData.get(i).getWordOfSentence().size(); j++) {
				String word = answerData.get(i).getWordOfSentence().get(j).getWord();				
				
				try {
					answerData.get(i).getWordOfSentence().get(j).setWordDenseVector(denseVector.get(word));
				}catch(Exception e) {
					answerData.get(i).getWordOfSentence().get(j).setWordDenseVector(avgDenseVector);
					System.err.println("Exception catched and handling :" + e.getMessage());
				}
					
			}
		}
		return answerData;
	}
	public void loadZeroToInitialDensevector(List<Double>avgDenseVector) {
		for (int i = 0; i < getDimensionSize(); i++) {
			avgDenseVector.add(0.0);
		}
	}
	public int findOccurrence(List<String> words, String word) {
		int count = 0;
		for (int i = 0; i < words.size(); i++) 
			if(word.equals(words.get(i).toString()))
				count++;
		return count;
	}
	public List<Integer> findOccIndex(List<String> words, String word){
		List <Integer> results = new ArrayList<>();
		for (int i = 0; i < words.size(); i++) 
			if(word.equals(words.get(i).toString()))
				results.add(i);		
		return results;
	}
}
