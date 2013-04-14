/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//package airline.system.resource;

/**
 *
 * @author Juntao Gu <juntao.gu@gmail.com>
 */
public class Result
{
    public Result()
    {
        resultString = new String();
        resultInt = -1;
        resultFlag = false;
        resultCommand = new Command();
    } 
    public Result(String resultString, int resultInt, boolean resultFlag, Command resultCommand)
    {
        this.resultString = resultString;
        this.resultInt = resultInt;
        this.resultFlag = resultFlag;
        this.resultCommand = resultCommand;
    }
    
    public String resultString;
    public int resultInt;
    public boolean resultFlag;
    public Command resultCommand;
}
