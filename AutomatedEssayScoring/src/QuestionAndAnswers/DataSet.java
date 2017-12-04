package QuestionAndAnswers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Person.Person;

public class DataSet {
	private List<Answer>answerDataSet;
	private Question answerDataSetQuestion;
	public DataSet() {
		// TODO Auto-generated constructor stub
	}
	public DataSet(String fileName) {
		answerDataSet = new ArrayList<Answer>();
		loadAndGenerateDataSet(fileName);
	}
	/**
	 * @return the answerDataSet
	 */
	public List<Answer> getAnswerDataSet() {
		return answerDataSet;
	}
	/**
	 * @param answerDataSet the answerDataSet to set
	 */
	public void setAnswerDataSet(List<Answer> answerDataSet) {
		this.answerDataSet = answerDataSet;
	}
	/**
	 * @return the answerDataSetQuestion
	 */
	public Question getAnswerDataSetQuestion() {
		return answerDataSetQuestion;
	}
	/**
	 * @param answerDataSetQuestion the answerDataSetQuestion to set
	 */
	public void setAnswerDataSetQuestion(Question answerDataSetQuestion) {
		this.answerDataSetQuestion = answerDataSetQuestion;
	}
	public void loadAndGenerateDataSet(String fileName) {
		String maxGrade = "100";
		try {
			FileInputStream file = new FileInputStream(fileName);
            @SuppressWarnings("resource")
			Scanner scanner = new Scanner(file,"UTF-8");
            String line = scanner.nextLine();
            answerDataSetQuestion = new Question(line);
            /*String args[] = line.split("\\+");
            maxGrade = args[1];
            System.out.println("readed axgeda :" + Double.parseDouble(maxGrade) + "\narg0 : " + args[0]);*/
            while(scanner.hasNextLine()){
                line = scanner.nextLine();
                String[] details = line.split(" ");
                int id = Integer.parseInt(details[0]);
                int grade = Integer.parseInt(details[1]);
                String answer = "";
                for (int i = 2; i < details.length; i++) {
                	if(i == details.length-1)
                		answer += details[i];
                	else
                		answer += details[i] + " ";
				}
                answerDataSet.add(new Answer(answer, grade, new Person(id)));
                //String[] details = line.split(" ");
                //System.out.println(line);
                //System.out.println(details[0] +" / "+details[1]);
                //getWords().put(details[0], Double.parseDouble(details[1]));
            }
        } catch (FileNotFoundException e) {  
        	System.err.println("Question and Answers file not found!");
            e.printStackTrace();
        }
	}
	@Override
	public String toString() {
		String result = answerDataSetQuestion.toString() + "\n";
		for (int i = 0; i < answerDataSet.size(); i++) 
			result += answerDataSet.get(i).toString();		
		return result;
	}
	public void AllAverageVector(){
		for (int i = 0; i < getAnswerDataSet().size(); i++) 
			getAnswerDataSet().get(i).genereteAverageVector();
	}
}
