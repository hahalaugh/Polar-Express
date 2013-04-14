/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//package airline.system.resource;

/**
 *
 * @author Juntao Gu <juntao.gu@gmail.com>
 */
public class Command
{
    public Command()
    {
        command = -1;
        argc = 0;
        argv = new String[0];
    }
    
    public Command(Command tempCommand)
    {
        this.argc = tempCommand.argc;
        this.argv = tempCommand.argv;
        this.command = tempCommand.command;
    }
    
    public Command(int tempCommand, String[] tempArgv, int tempArgc)
    {
        command = tempCommand;
        argv = tempArgv;
        argc = tempArgc;
    }

    @Override
    public String toString()
    {
        String string = new String("");
        for(int i = 0; i < argv.length; i ++)
        {
            if(argv[i] != null)
            {
                string += new String("Argv["+i+"]:"+argv[i].toString()+" ");
            }
            
        }
        return "Command{" + "command=" + command + ", argc=" + argc + ", argv=" + string + '}';
    }

    public int getCommand()
    {
        return command;
    }

    public int getArgc()
    {
        return argc;
    }

    public String[] getArgv()
    {
        return argv;
    }

    public void setCommand(int command)
    {
        this.command = command;
    }

    public void setArgc(int argc)
    {
        this.argc = argc;
    }

    public void setArgv(String[] argv)
    {
        this.argv = argv;
    }
    
    private int command;
    private int argc;
    private String[] argv;
}
