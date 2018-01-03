package Training;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

import Person.Person;
import QuestionAndAnswers.Answer;
import QuestionAndAnswers.DataSet;
import QuestionAndAnswers.Question;
import WordEmbedding.Embedding;

public class SecondMethod {
	private DataSet dataSet;
	private Embedding embedding;
	public String betasFileName = "./betas.txt";
	public Vector<Vector<Double>>dependValues;	
	public Vector<Vector<Double>>dataIndependedTable;							//sentenceCount X 200	(row,col)
	public Vector<Vector<Double>>dependValuesLast;	
	public Vector<Vector<Double>>dataIndependedTableLast;	
	public Vector<Double> betas;
	public Vector<Double> betasLast;
	public HashMap<Double, Answer> best;
	public int detailGrade;
	
	
	public SecondMethod() {
		// TODO Auto-generated constructor stub
	}
	public SecondMethod(String dataSetFileName, String embeddingFileName, String Word2VecOrFastText, int detailGrade) {
		setDataSet(new DataSet(dataSetFileName));
		setEmbedding(new Embedding(embeddingFileName, Word2VecOrFastText));		
		setDetailGrade(detailGrade);
	}
	/**
	 * @return the embedding
	 */
	public Embedding getEmbedding() {
		return embedding;
	}
	/**
	 * @param embedding the embedding to set
	 */
	public void setEmbedding(Embedding embedding) {
		this.embedding = embedding;
	}
	/**
	 * @return the dataSet
	 */
	public DataSet getDataSet() {
		return dataSet;
	}
	/**
	 * @param dataSet the dataSet to set
	 */
	public void setDataSet(DataSet dataSet) {
		this.dataSet = dataSet;
	}
	public int getDetailGrade() {
		return detailGrade;
	}
	public void setDetailGrade(int detailGrade) {
		this.detailGrade = detailGrade;
	}
	public void loadInitialDataTable() {
		dataIndependedTable = new Vector<Vector<Double>>();
		dependValues = new Vector<Vector<Double>>();
																	//grades
		
		for (int i = 0; i < getDataSet().getAnswerDataSet().size(); i++) {
			Vector<Double> temp = new Vector<>();
			for (int j = 0; j < 85; j++) {
				temp.add(getDataSet().getAnswerDataSet().get(i).getAverageWordVector().get(j));
			}
			/*for (int j = 0; j < getDataSet().getAnswerDataSet().get(i).getAverageWordVector().size(); j++) {
				temp.add(getDataSet().getAnswerDataSet().get(i).getAverageWordVector().get(j));
			}*/
			dataIndependedTable.addElement(temp);
			Vector <Double> tempDependVal = new Vector<>();
			tempDependVal.add(getDataSet().getAnswerDataSet().get(i).getGrade());
			dependValues.add(tempDependVal);
		}		
	}
	public void Regression() throws IOException {
        BufferedWriter output = null;
        try {
            File file = new File(betasFileName);
            output = new BufferedWriter(new FileWriter(file));
            //System.out.println("-------------------------------------------------------");
    		//Matrix matrixTransactions = new Matrix(dataIndependedTable, dependValues);
    		//Vector<Vector<Double>> result = matrixTransactions.lineerRegression();
    		Vector<Vector<Double>> result = MatrixDouble.generateRegression(dataIndependedTable, dependValues);
    		//System.out.println("Regression size :" + result.size());
    		for (int i = 0; i < result.size(); i++) {
    			for (int j = 0; j < result.get(i).size(); j++) {
    				//System.out.println(result.get(i).get(j));
    				String beta =  "" + result.get(i).get(j) + " ";
    				output.write(beta);
    			}
    		}
        } catch ( IOException e ) {
            e.printStackTrace();
        } finally {
          if ( output != null ) {
            output.close();
          }
        }
	}
	public void readBetasFromFile() {
		String line;
		betas = new Vector<>();
		try {
			FileInputStream file = new FileInputStream(betasFileName);
            @SuppressWarnings("resource")
			Scanner scanner = new Scanner(file,"UTF-8");
            while(scanner.hasNextLine()){
                line = scanner.nextLine();
                String[] details = line.split(" ");
                for (int i = 0; i < details.length; i++) {
                	betas.add(Double.parseDouble(details[i]));
				}
            }
        } catch (FileNotFoundException e) {  
        	System.err.println("Question and Answers file not found!");
            e.printStackTrace();
        }
	}
	public void trainDataSet() {
		getDataSet().setAnswerDataSet(embedding.vectorsFromCorpora(getDataSet().getAnswerDataSet()));
		getDataSet().AllAverageVector();
	}
	public void findRealError() throws IOException {
		double sum = 0.0;
		
		for (int i = 0; i < getDataSet().getAnswerDataSet().size(); i+=1) {
			Answer input = loadInitialDataTable(i);
			Regression();
			readBetasFromFile();
			sum += errorFromInIt(input);	
			System.out.println("all sum :" + sum);
		}
		System.out.println("All real without sqrt :" + sum);
		System.out.println("All real error :" + Math.sqrt(sum));
	}
	public void findRealErrorSecond() throws IOException {
		double sum = 0.0;
		
		for (int i = 0; i < getDataSet().getAnswerDataSet().size(); i+=1) {
			Answer input = loadInitialDataTable(i);
			Regression();
			readBetasFromFile();
			errorFromInIt(input);
			allDistanceToBestForAllSecond(i);
			RegressionLast();
			readBetasFromFileLast();
			/*for (double key : best.keySet()){
				System.out.println("key :" + key);
			}*/
			sum += calculateSecondMethodAccuracy(input);
			//System.out.println("all sum :" + sum);
		}
		//System.out.println("\nSystem Accurity :" + (100 - (sum/getDataSet().getAnswerDataSet().size())));
		System.out.println("\nSystem Accurity :" + (sum/getDataSet().getAnswerDataSet().size()));
	}
	public double calculateSecondMethodAccuracy(Answer input) {
		double yOld = 0.0;
		double subtractY = 0.0;
		double summation = 0.0;
		Vector<Double> temp = new Vector<>();
		yOld = input.getGrade();
		for (double key : best.keySet()) {
			temp.add(vecCosineSim(input, best.get(key)));
			
		}
		temp.add((double) input.getWordOfSentence().size());
		double pointlast = 0.0;
		for (int j = 0; j < betasLast.size(); j++) {
			pointlast += betasLast.get(j) * temp.get(j);
		}
		//System.out.println("////////////////////////// Second Method  //////////////////////////////");
		//System.out.println(input);
		if(getDetailGrade() == 1)
			System.out.println("Second Method Generated Grade :" + pointlast);
		else
			System.out.println("Second Method Generated Grade :" + Math.round(pointlast));
		/*subtractY = yOld - pointlast;
		if(subtractY < 0)
			subtractY *=-1;
		if(yOld == 0)
			yOld = 1.0;
		summation += (subtractY / yOld)*100;*/
		if(yOld > pointlast) {
			if(yOld == 0)
				yOld = 1;
			summation += ((double)pointlast / yOld) * 100;
		}else if(pointlast > yOld) {
			if(pointlast == 0)
				pointlast = 1;
			summation += ((double)yOld / pointlast) * 100;
		}else {
			summation += 100.0;
		}
		return summation;
	}
	public Answer loadInitialDataTable(int start) {
		dataIndependedTable = new Vector<Vector<Double>>();
		dependValues = new Vector<Vector<Double>>();
		List<Answer> res = new ArrayList<>();
		
		for (int i = 0; i < getDataSet().getAnswerDataSet().size(); i++) {
			Vector<Double> temp = new Vector<>();
			if(i >= start && i < start + 1) {
				res.add(getDataSet().getAnswerDataSet().get(i));
			}else {
				for (int j = 0; j < 85; j++) {		//53 idi
					temp.add(getDataSet().getAnswerDataSet().get(i).getAverageWordVector().get(j));
				}
				/*for (int j = 0; j < getDataSet().getAnswerDataSet().get(i).getAverageWordVector().size(); j++) {
					temp.add(getDataSet().getAnswerDataSet().get(i).getAverageWordVector().get(j));
				}*/
				dataIndependedTable.addElement(temp);
				Vector <Double> tempDependVal = new Vector<>();
				tempDependVal.add(getDataSet().getAnswerDataSet().get(i).getGrade());
				dependValues.add(tempDependVal);
			}
		}		
		return res.get(0);
	}
	public double errorFromInIt(Answer input) {
		double yOld = 0.0;
		double yNew = 0.0;
		double subtractY = 0.0;
		double summation = 0.0;
		best = new HashMap<>();
		yOld = input.getGrade();
		
		for(int j = 0; j < betas.size(); j++) {
			yNew += betas.get(j) * input.getAverageWordVector().get(j);
		}
		
		for (int j = 0; j < getDataSet().getAnswerDataSet().size(); j++) {
			if(input.getPerson().getId() != getDataSet().getAnswerDataSet().get(j).getPerson().getId()) {
				double bestGrade = getDataSet().getAnswerDataSet().get(j).getGrade();
				/*for(int i = 0; i < betas.size(); i++) {
					newest += betas.get(i) * input.getAverageWordVector().get(i);
				}*/
				if(!best.containsKey(bestGrade)) {
					best.put(bestGrade, getDataSet().getAnswerDataSet().get(j));
				}else {
					Answer answer = best.get(bestGrade);
					double bestNewest = 0.0;
					double newest = 0.0;
					for (int k = 0; k < betas.size(); k++) {
						bestNewest += betas.get(k) * answer.getAverageWordVector().get(k);
						newest += betas.get(k) * getDataSet().getAnswerDataSet().get(j).getAverageWordVector().get(k);
					}
					if((bestGrade - newest) < (bestGrade - bestNewest)) {
						best.put(bestGrade, getDataSet().getAnswerDataSet().get(j));
					}
				}
			}
		}
		
		subtractY = yOld - yNew;
		if(subtractY < 0)
			subtractY *=-1;
		System.out.println("------------------------------------------- Answer -------------------------------------------");
		System.out.println(getDataSet().getAnswerDataSetQuestion());
		System.out.println(input);
		System.out.println("First Method Generated Grade :" + yNew);
		//System.out.println();
		if(yOld == 0)
			yOld = 0.5;
		summation += subtractY / yOld;
		//System.out.println("summ :" + summation);
		//summation += Math.pow(subtractY, 2);
		yNew = 0.0;
		
		return summation;
	}
	public void printBest() {
		System.out.println("*********************** BESTS **************************");
		System.out.println(best.get(0.0));
		System.out.println(best.get(1.0));
		System.out.println(best.get(2.0));
		System.out.println(best.get(3.0));
		System.out.println(best.get(4.0));
		System.out.println(best.get(5.0));
	}
	public void allDistanceToBest() {
		dataIndependedTableLast = new Vector<Vector<Double>>();
		dependValuesLast = new Vector<Vector<Double>>();
		for (int i = 0; i < getDataSet().getAnswerDataSet().size(); i++) {
			Vector<Double> temp = new Vector<>();
			if(!isBestIn(getDataSet().getAnswerDataSet().get(i))) {
				for(double key :best.keySet()) {
					temp.add(vecDist(getDataSet().getAnswerDataSet().get(i), best.get(key)));	
				}
				dataIndependedTableLast.addElement(temp);
				Vector <Double> tempDependVal = new Vector<>();
				tempDependVal.add(getDataSet().getAnswerDataSet().get(i).getGrade());
				dependValuesLast.add(tempDependVal);
			}
		}
	}
	public void allDistanceToBestForAll() {
		dataIndependedTableLast = new Vector<Vector<Double>>();
		dependValuesLast = new Vector<Vector<Double>>();
		for (int i = 0; i < getDataSet().getAnswerDataSet().size(); i++) {
			Vector<Double> temp = new Vector<>();
			//if(!isBestIn(getDataSet().getAnswerDataSet().get(i))) {
				for(double key :best.keySet()) {
					temp.add(vecCosineSim(getDataSet().getAnswerDataSet().get(i), best.get(key)));	
					
				}
				temp.add((double) getDataSet().getAnswerDataSet().get(i).getWordOfSentence().size());
				dataIndependedTableLast.addElement(temp);
				Vector <Double> tempDependVal = new Vector<>();
				tempDependVal.add(getDataSet().getAnswerDataSet().get(i).getGrade());
				dependValuesLast.add(tempDependVal);	
			//}
		}
	}
	public List<Answer> allDistanceToBestForAllSecond(int start) {
		dataIndependedTableLast = new Vector<Vector<Double>>();
		dependValuesLast = new Vector<Vector<Double>>();
		List<Answer> res = new ArrayList<>();
		for (int i = 0; i < getDataSet().getAnswerDataSet().size(); i++) {
			Vector<Double> temp = new Vector<>();
			if(i >= start && i < start + 1) {
				res.add(getDataSet().getAnswerDataSet().get(i));
			}else {
				for(double key :best.keySet()) {
					temp.add(vecCosineSim(getDataSet().getAnswerDataSet().get(i), best.get(key)));						
				}
				temp.add((double) getDataSet().getAnswerDataSet().get(i).getWordOfSentence().size());
				dataIndependedTableLast.addElement(temp);
				Vector <Double> tempDependVal = new Vector<>();
				tempDependVal.add(getDataSet().getAnswerDataSet().get(i).getGrade());
				dependValuesLast.add(tempDependVal);	
			}
		}
		return res;
	}
	public boolean isBestIn(Answer input) {
		if(!best.containsKey(input.getGrade()))
			return false;
		for(double key :best.keySet()) {
			if(best.get(key).getSentence() == input.getSentence())
				return true;
		}
		return false;
	}
	public double vecCosineSim(Answer answer1, Answer answer2) {
		double dotProduct = 0.0;
		double answer1EuclideanSpace = 0.0;
		double answer2EuclideanSpace = 0.0;
		for (int i = 0; i < answer1.getAverageWordVector().size(); i++) {
			dotProduct += answer1.getAverageWordVector().get(i) * answer2.getAverageWordVector().get(i);
			answer1EuclideanSpace += Math.pow(answer1.getAverageWordVector().get(i), 2);
			answer2EuclideanSpace += Math.pow(answer2.getAverageWordVector().get(i), 2);
		}
		return dotProduct / (Math.sqrt(answer1EuclideanSpace) * Math.sqrt(answer2EuclideanSpace));
	}
	public double vecDist(Answer answer1, Answer answer2) {
		double vectoralDistance = 0.0;
		double distance = 0.0;
		for (int i = 0; i < answer1.getAverageWordVector().size(); i++) {
			vectoralDistance = 0.0;
			distance = answer1.getAverageWordVector().get(i) - answer2.getAverageWordVector().get(i);
			/*if(distance < 0)
				distance *= -1;*/
			//vectoralDistance += Math.pow(distance,2);
			vectoralDistance += distance;
		}
		return distance;
	}
	public void RegressionLast() throws IOException {
        BufferedWriter output = null;
        try {
            File file = new File("lastBetas.txt");
            output = new BufferedWriter(new FileWriter(file));
            //System.out.println("-------------------------------------------------------");
    		//Matrix matrixTransactions = new Matrix(dataIndependedTable, dependValues);
    		//Vector<Vector<Double>> result = matrixTransactions.lineerRegression();
    		Vector<Vector<Double>> result = MatrixDouble.generateRegression(dataIndependedTableLast, dependValuesLast);
    		//System.out.println("Regression size :" + result.size());
    		for (int i = 0; i < result.size(); i++) {
    			for (int j = 0; j < result.get(i).size(); j++) {
    				//System.out.println(result.get(i).get(j));
    				String beta =  "" + result.get(i).get(j) + " ";
    				output.write(beta);
    			}
    		}
        } catch ( IOException e ) {
            e.printStackTrace();
        } finally {
          if ( output != null ) {
            output.close();
          }
        }
	}
	public void readBetasFromFileLast() {
		String line;
		betasLast = new Vector<>();
		try {
			FileInputStream file = new FileInputStream("lastBetas.txt");
            @SuppressWarnings("resource")
			Scanner scanner = new Scanner(file,"UTF-8");
            while(scanner.hasNextLine()){
                line = scanner.nextLine();
                String[] details = line.split(" ");
                for (int i = 0; i < details.length; i++) {
                	betasLast.add(Double.parseDouble(details[i]));
				}
            }
        } catch (FileNotFoundException e) {  
        	System.err.println("Question and Answers file not found!");
            e.printStackTrace();
        }
	}
	public void guessQuestionPointLast(List<Answer> answerList) {
		double point = 0.0;

		answerList = embedding.vectorsFromCorpora(answerList);
		for (int i = 0; i < answerList.size(); i++) {
			answerList.get(i).genereteAverageVector();//cahnged
			Vector<Double> temp = new Vector<>();
			for (double key : best.keySet()) {
				temp.add(vecDist(answerList.get(i), best.get(key)));
			}
			double pointlast = 0.0;
			for (int j = 0; j < betasLast.size(); j++) {
				pointlast += betasLast.get(j) * temp.get(j);
			}
			System.out.println("////////////////////////// Second Method  //////////////////////////////");
			System.out.println(answerList.get(i));
			if(getDetailGrade() == 1)
				System.out.println("Second Method Generated Grade :" + pointlast);
			else
				System.out.println("Second Method Generated Grade :" + Math.round(pointlast));
		}	
	}
	public double getTeachersCorelation(String firstTeacher, String secondTeacher) {
		List<Integer> firstTeachersGrade = new ArrayList<>();
		List<Integer> secondTeachersGrade = new ArrayList<>();
		double corelation = 0.0;
		
		try {
			FileInputStream file = new FileInputStream(firstTeacher);
            @SuppressWarnings("resource")
			Scanner scanner = new Scanner(file,"UTF-8");
            String line = scanner.nextLine();
            while(scanner.hasNextLine()){
                line = scanner.nextLine();
                String[] details = line.split(" ");   
                firstTeachersGrade.add(Integer.parseInt(details[1]));
            }
        } catch (FileNotFoundException e) {  
        	System.err.println("Question and Answers file not found!");
            e.printStackTrace();
        }
		////////////////////////////////////////////////////////////
		try {
			FileInputStream file = new FileInputStream(secondTeacher);
            @SuppressWarnings("resource")
			Scanner scanner = new Scanner(file,"UTF-8");
            String line = scanner.nextLine();
            while(scanner.hasNextLine()){
                line = scanner.nextLine();
                String[] details = line.split(" ");   
                secondTeachersGrade.add(Integer.parseInt(details[1]));
            }
        } catch (FileNotFoundException e) {  
        	System.err.println("Question and Answers file not found!");
            e.printStackTrace();
        }
		
		for (int j = 0; j < secondTeachersGrade.size() && j < firstTeachersGrade.size(); j++) {
			
			if(firstTeachersGrade.get(j) > secondTeachersGrade.get(j)) {
				if(firstTeachersGrade.get(j) == 0)
					firstTeachersGrade.set(j, 1);
				corelation += ((double)secondTeachersGrade.get(j) / firstTeachersGrade.get(j)) * 100;
			}else if(secondTeachersGrade.get(j) > firstTeachersGrade.get(j)) {
				if(secondTeachersGrade.get(j) == 0)
					secondTeachersGrade.set(j, 1);
				corelation += ((double)firstTeachersGrade.get(j) / secondTeachersGrade.get(j)) * 100;
			}else {
				corelation += 100.0;
			}
		}
		return corelation / secondTeachersGrade.size();
	}
	public double corelation(double first, double second) {
		double corelation = 0.0;
		
		if(first > second) {
			if(first == 0)
				first = 1;
			corelation += ((double)second / first) * 100;
		}else if(second > first) {
			if(second == 0)
				second = 1;
			corelation += ((double)first / second) * 100;
		}else {
			corelation += 100.0;
		}
		return corelation;
	}
	public void guessQuestionPointLastCosine(List<Answer> answerList) {
		double point = 0.0;

		answerList = embedding.vectorsFromCorpora(answerList);
		for (int i = 0; i < answerList.size(); i++) {
			answerList.get(i).genereteAverageVector();//cahnged
			Vector<Double> temp = new Vector<>();
			for (double key : best.keySet()) {
				temp.add(vecCosineSim(answerList.get(i), best.get(key)));
				
			}
			temp.add((double) answerList.get(i).getWordOfSentence().size());
			double pointlast = 0.0;
			for (int j = 0; j < betasLast.size(); j++) {
				pointlast += betasLast.get(j) * temp.get(j);
			}
			System.out.println("////////////////////////// Second Method  //////////////////////////////");
			System.out.println(answerList.get(i));
			if(getDetailGrade() == 1)
				System.out.println("Second Method Generated Grade :" + pointlast);
			else
				System.out.println("Second Method Generated Grade :" + Math.round(pointlast));
		}	
	}
}