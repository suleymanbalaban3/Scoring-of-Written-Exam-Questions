package QuestionAndAnswers;

import Person.Person;
import Sentence.Sentence;

public class Answer extends Sentence {
	private Person person;
	private double grade;
	private double maxGrade;
	
	public Answer() {
		
	}
	public Answer(String answer) {
		super(answer);
		setPerson(new Person(999));
	}
	public Answer(String answer, double grade) {
		super(answer);
		setGrade(grade);
		setPerson(new Person(999));
	}
	public Answer(String answer, double grade, Person person) {
		super(answer);
		setGrade(grade);
		setPerson(person);
	}
	public Answer(String answer, double grade, Person person, double maxGrade) {
		super(answer);
		setGrade(grade);
		setPerson(person);
		setMaxGrade(maxGrade);
	}
	/**
	 * @return the grade
	 */
	public double getGrade() {
		return grade;
	}
	/**
	 * @param grade the grade to set
	 */
	public void setGrade(double grade) {
		this.grade = grade;
	}
	/**
	 * @return the person
	 */
	public Person getPerson() {
		return person;
	}
	/**
	 * @param person the person to set
	 */
	public void setPerson(Person person) {
		this.person = person;
	}
	@Override
	public String toString() {
		return getPerson() + "\nSentence :" + getSentence()+"\nGrade :" +getGrade();
	}
	/**
	 * @return the maxGrade
	 */
	public double getMaxGrade() {
		return maxGrade;
	}
	/**
	 * @param maxGrade the maxGrade to set
	 */
	public void setMaxGrade(double maxGrade) {
		this.maxGrade = maxGrade;
	}
}
