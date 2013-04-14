/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//package airline.system.resource;


/**
 *
 * @author Juntao Gu <juntao.gu@gmail.com>
 */
public class Bulletin
{

    public Bulletin()
    {
        
    }
    
    public Bulletin(int bulletinAnswerNumber, String bulletinAnswerFrom, String bulletinAnswerTo, String bulletinQuestion, String bulletinAnswer)
    {
        this.bulletinAnswerNumber = bulletinAnswerNumber;
        this.bulletinAnswerFrom = bulletinAnswerFrom;
        this.bulletinAnswerTo = bulletinAnswerTo;
        this.bulletinQuestion = bulletinQuestion;
        this.bulletinAnswer = bulletinAnswer;
    }

    public long getBulletinAnswerNumber()
    {
        return bulletinAnswerNumber;
    }

    public String getBulletinAnswerFrom()
    {
        return bulletinAnswerFrom;
    }

    public String getBulletinAnswerTo()
    {
        return bulletinAnswerTo;
    }

    public String getBulletinQuestion()
    {
        return bulletinQuestion;
    }

    public String getBulletinAnswer()
    {
        return bulletinAnswer;
    }

    public void setBulletinAnswerNumber(int bulletinAnswerNumber)
    {
        this.bulletinAnswerNumber = bulletinAnswerNumber;
    }

    public void setBulletinAnswerFrom(String bulletinAnswerFrom)
    {
        this.bulletinAnswerFrom = bulletinAnswerFrom;
    }

    public void setBulletinAnswerTo(String bulletinAnswerTo)
    {
        this.bulletinAnswerTo = bulletinAnswerTo;
    }

    public void setBulletinQuestion(String bulletinQuestion)
    {
        this.bulletinQuestion = bulletinQuestion;
    }

    public void setBulletinAnswer(String bulletinAnswer)
    {
        this.bulletinAnswer = bulletinAnswer;
    }

    @Override
    public String toString()
    {
        return "bulletinAnswerNumber: " + bulletinAnswerNumber+ "" + "\n"
                + "bulletinAnswerFrom: " + bulletinAnswerFrom + "\n"
                + "bulletinAnswerTo: " + bulletinAnswerTo + "\n"
                + "bulletinQuestion: " + bulletinQuestion + "\n"
                + "bulletinAnswer: " + bulletinAnswer + "\n";
                
    }
    
    private int bulletinAnswerNumber;
    private String bulletinAnswerFrom;
    private String bulletinAnswerTo;
    private String bulletinQuestion;
    private String bulletinAnswer;
}