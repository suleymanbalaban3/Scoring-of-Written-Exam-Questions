package Training;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

import Person.Person;
import QuestionAndAnswers.Answer;
import QuestionAndAnswers.DataSet;
import QuestionAndAnswers.Question;
import WordEmbedding.Embedding;

public class LineerRegression {
	private DataSet dataSet;
	private Embedding embedding;
	public String betasFileName = "./betas.txt";
	public Vector<Vector<Double>>dependValues;	
	public Vector<Vector<Double>>dataIndependedTable;							//sentenceCount X 200	(row,col)
	public Vector<Double> betas;
	
	public LineerRegression() {
		// TODO Auto-generated constructor stub
	}
	public LineerRegression(String dataSetFileName, String embeddingFileName, String Word2VecOrFastText) {
		dataSet = new DataSet(dataSetFileName);
		embedding = new Embedding(embeddingFileName, Word2VecOrFastText);
		
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
	/**
	 * @return the embdedding
	 */
	public Embedding getEmbdedding() {
		return embedding;
	}
	/**
	 * @param embdedding the embdedding to set
	 */
	public void setEmbdedding(Embedding embdedding) {
		this.embedding = embdedding;
	}
	public void trainDataSet() {
		getDataSet().setAnswerDataSet(embedding.vectorsFromCorpora(getDataSet().getAnswerDataSet()));
		getDataSet().AllAverageVector();
		/*trainDataSet();*/
		//loadInitialDataTable();
		//printInitialDataTable();	
	}
	
	public void loadInitialDataTable() {
		dataIndependedTable = new Vector<Vector<Double>>();
		dependValues = new Vector<Vector<Double>>();
																	//grades
		
		for (int i = 0; i < getDataSet().getAnswerDataSet().size(); i++) {
			Vector<Double> temp = new Vector<>();
			for (int j = 0; j < 85; j++) {		//60 idi
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
	public void printInitialDataTable() {
		for (int i = 0; i < dataIndependedTable.size(); i++) {
			System.out.println();
			for (int j = 0; j < dataIndependedTable.get(i).size(); j++) {
				System.out.print(dataIndependedTable.get(i).get(j)+" ");
			}
			System.out.println("\n");
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
	/*public void generateRandomBeta() {
		betas = new Vector<>(200);
		for (int i = 0; i < 200; i++) {
			double rangeMin = -2;
			double rangeMax = -1;
			Random r = new Random();
			double randomValue = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
			betas.add(randomValue);
		}
	}*/
	public void guessQuestionPoint(List<Answer> answerList) {
		double point = 0.0;

		answerList = embedding.vectorsFromCorpora(answerList);
		for (int i = 0; i < answerList.size(); i++) {
			answerList.get(i).genereteAverageVector();//cahnged
		}

		for(int j = 0; j < answerList.size(); j++) {
			for (int i = 0; i < betas.size(); i++) {	
				point += betas.get(i) * answerList.get(j).getAverageWordVector().get(i);
			}			
			
			answerList.get(j).setGrade(point);
			System.out.println(answerList.get(j));			
			point = 0;
		}		
	}
	public void findRealError() throws IOException {
		double sum = 0.0;
		
		for (int i = 0; i < getDataSet().getAnswerDataSet().size(); i+=1) {
			List<Answer> input = loadInitialDataTable(i);
			Regression();
			readBetasFromFile();
			sum += errorFromInIt(input);	
			System.out.println("all sum :" + sum);
		}
		System.out.println("Accurity :" + sum/getDataSet().getAnswerDataSet().size());
	}
	public List<Answer> loadInitialDataTable(int start) {
		dataIndependedTable = new Vector<Vector<Double>>();
		dependValues = new Vector<Vector<Double>>();
		List<Answer> res = new ArrayList<>();
		
		for (int i = 0; i < getDataSet().getAnswerDataSet().size(); i++) {
			Vector<Double> temp = new Vector<>();
			if(i >= start && i < start + 1) {
				res.add(getDataSet().getAnswerDataSet().get(i));
			}else {
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
		return res;
	}
	public double errorFromInIt(List<Answer> input) {
		double yOld = 0.0;
		double yNew = 0.0;
		double subtractY = 0.0;
		double summation = 0.0;
		for(int i = 0; i < input.size(); i++) {
			yOld = input.get(i).getGrade();
			for(int j = 0; j < betas.size(); j++) {
				yNew += betas.get(j) * input.get(i).getAverageWordVector().get(j);
			}
			
			subtractY = yOld - yNew;
			
			if(subtractY < 0)
				subtractY *=-1;
			System.out.println("------------------------------------------- Answer -------------------------------------------");
			System.out.println(getDataSet().getAnswerDataSetQuestion());
			System.out.println(input.get(i));
			System.out.println("Estimeted :" + yNew);
			System.out.println();
			if(yOld == 0)
				yOld = 1.0;
			summation += (subtractY / yOld)*100;
			System.out.println("summ :" + summation);
			//summation += Math.pow(subtractY, 2);
			yNew = 0.0;
		}
		return summation;
	}
	public double errorFromInIt2() {
		double yOld = 0.0;
		double yNew = 0.0;
		double subtractY = 0.0;
		double summation = 0.0;
		double yOldSum = 0.0;
		double yNewSum = 0.0;
		for(int i = 0; i < getDataSet().getAnswerDataSet().size(); i++) {
			yOld = getDataSet().getAnswerDataSet().get(i).getGrade();
			for(int j = 0; j < betas.size(); j++) {
				yNew += betas.get(j) * getDataSet().getAnswerDataSet().get(i).getAverageWordVector().get(j);
			}
			yNewSum += yNew;
			yOldSum += yOld;
			subtractY = yOld - yNew;
			
			if(subtractY < 0)
				subtractY *=-1;
			System.out.println("------------------------------------------- Answer -------------------------------------------");
			System.out.println(getDataSet().getAnswerDataSetQuestion());
			System.out.println(getDataSet().getAnswerDataSet().get(i));
			System.out.println("Estimeted :" + yNew);
			System.out.println();
			summation += subtractY / yOld;
			yNew = 0.0;
		}
		
		return (summation);
	}
}
