/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//package airline.system.resource;

/**
 *
 * @author Juntao Gu <juntao.gu@gmail.com>
 */
public class User
{
    public User()
    {
        
    }
    
    public User(int userID, String userAccountName, String userPassword, String userName, String userEmailAddress, String userPassportID)
    {
        this.userID = userID;
        this.userAccountName = userAccountName;
        this.userPassword = userPassword;
        this.userName = userName;
        this.userEmailAddress = userEmailAddress;
        this.userPassportID = userPassportID;
    }

    public int getUserID()
    {
        return userID;
    }

    public String getUserAccountName()
    {
        return userAccountName;
    }

    public String getUserPassword()
    {
        return userPassword;
    }

    public String getUserName()
    {
        return userName;
    }

    public String getUserEmailAddress()
    {
        return userEmailAddress;
    }

    public String getUserPassportID()
    {
        return userPassportID;
    }

    public void setUserID(int userID)
    {
        this.userID = userID;
    }

    public void setUserAccountName(String userAccountName)
    {
        this.userAccountName = userAccountName;
    }

    public void setUserPassword(String userPassword)
    {
        this.userPassword = userPassword;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public void setUserEmailAddress(String userEmailAddress)
    {
        this.userEmailAddress = userEmailAddress;
    }

    public void setUserPassportID(String userPassportID)
    {
        this.userPassportID = userPassportID;
    }

    @Override
    public String toString()
    {
        return "userID: " + userID + "\n"
                + "userAccountName: " + userAccountName + "\n"
                + "userPassword: " + userPassword + "\n"
                + "userName: " + userName + "\n"
                + "userEmailAddress: " + userEmailAddress + "\n"
                + "userPassportID: " + userPassportID +"\n";
    }
    
    private int userID;
    private String userAccountName;
    private String userPassword;
    private String userName;
    private String userEmailAddress;
    private String userPassportID;
}
