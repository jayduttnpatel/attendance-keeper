/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jaydutt
 */
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import javax.swing.JOptionPane;

public class SendEmail implements Runnable {

    public static final int SIGN_IN=1;
    
    public static final int LOG_IN=2;
    
    private static String subject="Attandace Keeper";
    
    private static String admin_email="******";
    
    private static String admin_pass="******";
    
    private Thread t;
    
    private static int type;
    
    private static String name;
    
    private static String to; 
    
    public SendEmail(int type,String name,String to)
    {
        t=new Thread(this);
        t.start();
        this.type=type;
        this.name=name;
        this.to=to;
    }
    public boolean sendMail()
    {
        String message=makeMessage();
        
        if(message==null)
        {
            return false;
        }
        
        send(message);
        
        return true;
    }
    
    private String makeMessage()
    {
        String message=null;
        
        String date,time;
        date = DateAndTimeUtils.getDate();
        time= DateAndTimeUtils.getTime();
        
        if(type==SIGN_IN)
        {
            message="Hi "+name+",\n"+"Thank you for joining our family.Your account has successfully been created.\n"+
                    "Name   : "+name+"\n"+
                    "emil id: "+to+"\n"+
                    "Date   : "+date+"\n"+
                    "Time   : "+time+"\n\n\n"+
                    "Thanks,\nTeam of Attandance keeper";
        }
        else if(type==LOG_IN)
        {
            message="Hi "+name+",\n"+"Your attendance has successfully been added to your account.\n"+
                    "Name   : "+name+"\n"+
                    "emil id: "+to+"\n"+
                    "Date   : "+date+"\n"+
                    "Time   : "+time+"\n\n\n"+
                    "Thanks,\nTeam of Attandance keeper";
        }
        return message;
    }
    
    public void send(String msg) 
    {
        Properties props = new Properties();

        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");	
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        
        Session session = Session.getDefaultInstance(props,new Authenticator() 
        {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() 
            {
                return new PasswordAuthentication(admin_email, admin_pass);
            }
        });

        try 
        {
            Message message = new MimeMessage(session);
            
            message.setFrom(new InternetAddress(admin_email));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(msg);

            Transport.send(message);
            
            JOptionPane.showMessageDialog(null,"Email sended!");
            
        } catch (MessagingException e) 
        {
            JOptionPane.showMessageDialog(null,"Something happened!");
            
            throw new RuntimeException(e);
        }
        
    }

    @Override
    public void run() {
        sendMail();
    }
}
