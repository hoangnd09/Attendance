package co.kr.soosan.attendance;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

	// ***** SETTING SECTION, will be moved to application setting *****
	private String smtp_host = "mail.soosan.co.kr";
	private String smtp_port = "25";
	private String smtp_auth = "false";
	private String smtp_username = "hoangnguyen";
	private String smtp_password = "qwe123";
	
	private String sender = "hoangnguyen@soosan.co.kr";
	// private String recipient = "hoangnguyen@soosan.co.kr";
	private String recipient = "maichi@soosan.co.kr";
	
	// ***** END SETTING SECTION *****
	
	private static final int TOAST_DURATION = 500;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        
        final Button sendAttendance = (Button) this.findViewById(R.id.sendAttendance);
        sendAttendance.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	String currentDateTimeString = new SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.US).format(new Date());
            	String subject = "[Attending Report] " + currentDateTimeString;

            	boolean success = sendMail(subject, "Regards");
            	if (success) {
                    Toast.makeText(getBaseContext(), "sending success", TOAST_DURATION).show();
            	} else {
            		Toast.makeText(getBaseContext(), "sending failure", TOAST_DURATION).show();
            	}
            }
        });
        
        final Button sendLeave = (Button) this.findViewById(R.id.sendLeave);
        sendLeave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	String currentDateTimeString = new SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.US).format(new Date());
            	String subject = "[Leaving Report] " + currentDateTimeString;

            	boolean success = sendMail(subject, "Regards");
            	if (success) {
                    Toast.makeText(getBaseContext(), "sending success", TOAST_DURATION).show();
            	} else {
            		Toast.makeText(getBaseContext(), "sending failure", TOAST_DURATION).show();
            	}
            }
        });
        
    }
    
    private boolean sendMail (String subject, String content) {
    	// https://javamail.java.net/nonav/docs/api/com/sun/mail/smtp/package-summary.html
    	Properties props = new Properties();
		props.put("mail.smtp.host", this.smtp_host);
		props.put("mail.smtp.port", this.smtp_port);
		props.put("mail.smtp.auth", this.smtp_auth);
		props.put("mail.smtp.socketFactory.port", this.smtp_port);
		props.put("mail.smtp.socketFactory.class", "mail.smtp.socketFactory");
		
		Session session = Session.getDefaultInstance(props,
			new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(MainActivity.this.smtp_username, MainActivity.this.smtp_password);
				}
			});

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(this.sender));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(this.recipient));
			message.setSubject(subject);
			message.setText(content);
			message.setSentDate(new Date());
	
			Transport.send(message);
			return true;
		} catch (MessagingException e) {
			return false;
		} finally {
			
		}
	}
    
    
    
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Log.v(this.getClass().getName(), "goto setting screen");
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
