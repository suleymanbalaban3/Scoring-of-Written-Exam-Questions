package UI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import QuestionAndAnswers.Answer;
import Training.LineerRegression;
import WordEmbedding.Embedding;

public class Test {

	public Test() {
		// TODO Auto-generated constructor stub
	}
	public static void main(String[] args) throws IOException {
		String dataSetFile = "./QuestionAndAnswer1Biology.txt";
		String embedinngFileWord2Vec = "./vectorsLast.txt";					//word2vec
		String embedinngFileFastText = "./vectorsLastFastText.txt";			//fasttext
		
		//LineerRegression lineerRegression = new LineerRegression(dataSetFile, embedinngFileFastText, "FastText");
		LineerRegression lineerRegression = new LineerRegression(dataSetFile, embedinngFileWord2Vec, "Word2Vec");
		lineerRegression.trainDataSet();
		System.out.println("--------------------------------------");
		lineerRegression.Regression();                        		//zaman alir beta deðerleri varsa çalýþtýrma
		lineerRegression.readBetasFromFile();						//betalr file da ise okunur ustteki fonksiyon calistirilmaz
		Answer answer = new Answer("Protein sindiriminde zorlanır.");
		answer.setMaxGrade(5);
		Answer answer1 = new Answer("Midede protein ilk sindirimi yapılır. Bu yüzden büyük protein moleküllerini alamaz. Besinler burada bulamaç halindeydi ince bağırsağa  direk geçer. Katı moleküller alınamaz bu yüzden. Aşırı zayıflama görülür. Metabolizma yavaşlar.");
		answer1.setMaxGrade(5);
		Answer answer2 = new Answer("Yemek borusunun bir kısmı  mide gibi davranacaktır ancak proteinlerin sindirimi mideye gelen enzimler ile başladığı için protein sindiriminde sorun yaşar.");
		answer2.setMaxGrade(5);
		Answer answer3= new Answer("Yemek yemede zorlanma. Midede yanma.");
		answer3.setMaxGrade(5);
		Answer answer4 = new Answer("Bilmiyorum ne cevap verecek.");
		answer4.setMaxGrade(5);
		System.out.println("--------------------------------------");
		System.out.println("Question :" + lineerRegression.getDataSet().getAnswerDataSetQuestion().getSentence());
		List<Answer> answerList = new ArrayList<>();
		answerList.add(answer);
		answerList.add(answer1);
		answerList.add(answer2);
		answerList.add(answer3);
		answerList.add(answer4);
		lineerRegression.guessQuestionPoint(answerList);				//hesaplama icin
		//System.out.println(answer);
	}
}