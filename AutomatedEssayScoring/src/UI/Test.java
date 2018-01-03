package UI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import QuestionAndAnswers.Answer;
import Training.LineerRegression;
import Training.SecondMethod;
import WordEmbedding.Embedding;

public class Test {
	public static String dataSetFile = "./QuestionAndAnswer1Biology.txt";
	public static String dataSetFileSuleyman = "./suleymanResults.txt";
	public static String dataSetFileEyup = "./eyupResults.txt";
	public static String dataSetFileYesim = "./yesimResults.txt";
	public static String dataSetFileAysenur = "./aysenurResults.txt";
	public static String embedinngFileWord2Vec = "./vectorsLast.txt";					//word2vec
	public static String embedinngFileFastText = "./vectorsLastFastText.txt";			//fasttext
	public Test() {
		// TODO Auto-generated constructor stub
	}
	public static void main(String[] args) throws IOException {
		
		tester1ForFirstInputs(dataSetFile, embedinngFileWord2Vec, "Word2Vec", 0);							//newest1 method
		tester2ForFirstMethodAccuracy(dataSetFileSuleyman, embedinngFileFastText, "FastText");				//newest2 method
		tester3ForSecondInputs(dataSetFile, embedinngFileWord2Vec, "Word2Vec", 0);							//newest3 method
		tester4ForSecondMethodAccuracy(dataSetFileSuleyman, embedinngFileFastText, "FastText", 0);			//newest4 method
		tester5ForTeacherCorelation(dataSetFile, dataSetFileSuleyman, embedinngFileWord2Vec, "Word2Vec", 0);//newest5 method
		
	}
	public static void tester1ForFirstInputs(String datasetFile, String embedinngFile, String status, int detailGrade) throws IOException {
		LineerRegression lineerRegression = new LineerRegression(datasetFile, embedinngFile, status); 
		lineerRegression.trainDataSet();
		lineerRegression.loadInitialDataTable();
		lineerRegression.Regression();                        		
		lineerRegression.readBetasFromFile();	
		
		Answer answer = new Answer("Protein sindiriminde zorlanır.");
		answer.setMaxGrade(5);
		Answer answer1 = new Answer("Midede protein ilk sindirimi yapılır bu yüzden büyük protein moleküllerini alamaz. Besinler burada bulamaç halindeydi ince bağırsağa  direk geçer. Katı moleküller alınamaz bu yüzden. Aşırı zayıflama görülür. Metabolizma yavaşlar.");
		answer1.setMaxGrade(5);
		Answer answer2 = new Answer("Yemek borusunun bir kısmı  mide gibi davranacaktır ancak proteinlerin sindirimi mideye gelen enzimler ile başladığı için protein sindiriminde sorun yaşar.");
		answer2.setMaxGrade(5);
		Answer answer3= new Answer("Mide alındığında protein sindirimi gerçekleşmez. Protein yapısal bir besin olduğu için insan ölebilir. Ayrıca midede bulunan enzimler bazı sindirim sistemi organlarını da çalıştırır mide giderse tüm sindirim sistemi çöker.");
		answer3.setMaxGrade(5);

		
		List<Answer> answerList = new ArrayList<>();
		answerList.add(answer);
		answerList.add(answer1);
		answerList.add(answer2);
		answerList.add(answer3);
		lineerRegression.guessQuestionPoint(answerList);
		//secondMethod.foo(answerList);
	}
	public static void tester2ForFirstMethodAccuracy(String datasetFile, String embedinngFileWord2Vec, String status) throws IOException {
		LineerRegression lineerRegression = new LineerRegression(datasetFile, embedinngFileWord2Vec, status);  
		lineerRegression.trainDataSet();
		System.out.println("**********************testing**********************");
		lineerRegression.findRealError();
		System.out.println("********************finish testing*****************");
	}
	public static void tester3ForSecondInputs(String datasetFile, String embedinngFileWord2Vec, String status, int detailGrade) throws IOException {
		SecondMethod secondMethod = new SecondMethod(datasetFile, embedinngFileWord2Vec, status, detailGrade);
		secondMethod.trainDataSet();
		secondMethod.findRealError();
		secondMethod.printBest();
		secondMethod.allDistanceToBest();
		secondMethod.RegressionLast();
		secondMethod.readBetasFromFileLast();
		
		Answer answer = new Answer("Protein sindiriminde zorlanır.");
		answer.setMaxGrade(5);
		Answer answer1 = new Answer("Midede protein ilk sindirimi yapılır bu yüzden büyük protein moleküllerini alamaz. Besinler burada bulamaç halindeydi ince bağırsağa  direk geçer. Katı moleküller alınamaz bu yüzden. Aşırı zayıflama görülür. Metabolizma yavaşlar.");
		answer1.setMaxGrade(5);
		Answer answer2 = new Answer("Yemek borusunun bir kısmı  mide gibi davranacaktır ancak proteinlerin sindirimi mideye gelen enzimler ile başladığı için protein sindiriminde sorun yaşar.");
		answer2.setMaxGrade(5);
		Answer answer3= new Answer("Mide alındığında protein sindirimi gerçekleşmez. Protein yapısal bir besin olduğu için insan ölebilir. Ayrıca midede bulunan enzimler bazı sindirim sistemi organlarını da çalıştırır mide giderse tüm sindirim sistemi çöker.");
		answer3.setMaxGrade(5);
		Answer answer4 = new Answer("bilmiyorum ne cevap verecek.");
		answer4.setMaxGrade(5);
		
		List<Answer> answerList = new ArrayList<>();
		answerList.add(answer);
		answerList.add(answer1);
		answerList.add(answer2);
		answerList.add(answer3);
		answerList.add(answer4);
		secondMethod.guessQuestionPointLastCosine(answerList);
		//secondMethod.foo(answerList);
	}
	public static void tester4ForSecondMethodAccuracy(String datasetFile, String embedinngFileWord2Vec, String status, int detailGrade) throws IOException {
		SecondMethod secondMethod = new SecondMethod(datasetFile, embedinngFileWord2Vec, status, detailGrade);  
		secondMethod.trainDataSet();
		System.out.println("*****************testing******************");
		secondMethod.findRealErrorSecond();
		System.out.println("**************finish testing**************");
	}
	public static void tester5ForTeacherCorelation(String teacher1, String teacher2, String embedinngFileWord2Vec, String status, int detailGrade) throws IOException {
		SecondMethod secondMethod = new SecondMethod(teacher1, embedinngFileWord2Vec, status, detailGrade);  
		System.out.println("Teachers Corelation :" + secondMethod.getTeachersCorelation(teacher1, teacher2));
	}
}
