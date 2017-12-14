package UI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import QuestionAndAnswers.Answer;
import Training.LineerRegression;
import Training.SecondMethod;
import WordEmbedding.Embedding;

public class Test {

	public Test() {
		// TODO Auto-generated constructor stub
	}
	public static void main(String[] args) throws IOException {
		String dataSetFile = "./QuestionAndAnswer1Biology.txt";
		String dataSetFileSuleyman = "./suleymanResults.txt";
		String dataSetFileEyup = "./eyupResults.txt";
		String dataSetFileYesim = "./yesimResults.txt";
		String embedinngFileWord2Vec = "./vectorsLast.txt";					//word2vec
		String embedinngFileFastText = "./vectorsLastFastText.txt";			//fasttext
	/*
		//LineerRegression lineerRegression = new LineerRegression(dataSetFile, embedinngFileFastText, "FastText");
		LineerRegression lineerRegression = new LineerRegression(dataSetFileYesim, embedinngFileWord2Vec, "Word2Vec");
		lineerRegression.trainDataSet();
		//lineerRegression.loadInitialDataTable();
		lineerRegression.findRealError();
		/*System.out.println("--------------------------------------");
		lineerRegression.Regression();                        		//zaman alir beta deðerleri varsa çalýþtýrma
		lineerRegression.readBetasFromFile();						//betalr file da ise okunur ustteki fonksiyon calistirilmaz
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
		System.out.println("--------------------------------------");
		System.out.println("Question :" + lineerRegression.getDataSet().getAnswerDataSetQuestion().getSentence());
		List<Answer> answerList = new ArrayList<>();
		answerList.add(answer);
		answerList.add(answer1);
		answerList.add(answer2);
		answerList.add(answer3);
		answerList.add(answer4);
		lineerRegression.guessQuestionPoint(answerList);		*/		
		
		
		//tester3(dataSetFile, embedinngFileWord2Vec, "Word2Vec");		//newest3 method
		tester4(dataSetFileSuleyman, embedinngFileWord2Vec, "Word2Vec");		//newest4 method
		//System.out.println("son hata oranı :" + lineerRegression.errorFromInIt2());
		//System.out.println(answer);
		
	}
	public static void tester3(String datasetFile, String embedinngFileWord2Vec, String status) throws IOException {
		SecondMethod secondMethod = new SecondMethod(datasetFile, embedinngFileWord2Vec, status);
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
		secondMethod.guessQuestionPointLast(answerList);
		//secondMethod.foo(answerList);
	}
	public static void tester4(String datasetFile, String embedinngFileWord2Vec, String status) throws IOException {
		SecondMethod secondMethod = new SecondMethod(datasetFile, embedinngFileWord2Vec, status);
		secondMethod.trainDataSet();
		System.out.println("*****************testing******************");
		secondMethod.findRealErrorSecond();
		System.out.println("**************finish testing**************");
		/*secondMethod.findRealError();
		secondMethod.printBest();
		
		secondMethod.allDistanceToBestForAll();
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
		secondMethod.guessQuestionPointLastCosine(answerList);*/
		//secondMethod.foo(answerList);
	}
}
//61 4 Mide alındığında protein sindirimi gerçekleşmez. Protein yapısal bir besin olduğu için insan ölebilir. Ayrıca midede bulunan enzimler bazı sindirim sistemi organlarını da çalıştırır mide giderse tüm sindirim sistemi çöker.