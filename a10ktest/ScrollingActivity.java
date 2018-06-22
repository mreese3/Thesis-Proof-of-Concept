package tenk.test.devteam7.a10ktest;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuItem;
import java.util.Random;
import android.os.Handler;
import java.io.*;
import java.util.Timer;
import java.util.TimerTask;
import android.content.Context;

public class ScrollingActivity extends AppCompatActivity {

    float[] theData;
    TextView tv;
    final Handler myHandler = new Handler();
    int i = 0;

    boolean activityCompleteRefresh = false;
    boolean inGeneration = false;

    boolean initialStateComplete = false;

    boolean saveCompleteAction = false;

    boolean readFileComplete = false;

    boolean readingTheFile = false;

    ProgressDialog pd;

    String Maintext = "10,000 random 32 bit float generated.\n\n";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        if(initialStateComplete == false) {
            //TextView txtView = (TextView) findViewById(R.id.scrText);
            //txtView.setText("processing...");
            tv = (TextView) findViewById(R.id.scrText);


            tv.setText("...");

            theData = new float[10000];

            pd = new ProgressDialog(this);

            Timer myTimer = new Timer();
            myTimer.schedule(new TimerTask() {
                @Override
                public void run() {UpdateGUI();}
            }, 0, 500);



            //if file...
            if(fileExist("tenKfile.txt") == true) {
                readFile();
            }
            else {
                startGenerate();
            }


        }
    }

    private void writefile(){
        pd.setMessage("writing file");
        pd.show();
        pd.setCancelable(false);


        Runnable runnable = new Runnable() {
            public void run() {
                stringToFile(Maintext,"tenKfile.txt");
            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();


    }

    private void readFile(){
        pd.setMessage("reading file");
        pd.show();
        pd.setCancelable(false);


        Runnable runnable = new Runnable() {
            public void run() {
                readStringFile("tenKfile.txt");
            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();


    }

    private void startGenerate(){
        Runnable runnable = new Runnable() {
            public void run() {
                generateNumbers();
            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();



        pd.setMessage("generating numbers");
        pd.show();
        pd.setCancelable(false);

    }


    private void UpdateGUI() {
        i++;

        //tv.setText(String.valueOf(i));
        myHandler.post(myRunnable);
    }

    final Runnable myRunnable = new Runnable() {
        public void run() {
            //tv.setText(String.valueOf(i));
            if(inGeneration == true){
                tv.setText("generating: "+String.valueOf(i+1));

            }
            if(readingTheFile == true){
                tv.setText("reading: "+String.valueOf(i+1));
            }
            if(activityCompleteRefresh == true) {
                tv.setText(Maintext);
                activityCompleteRefresh = false;

                pd.hide();

                writefile();
            }

            if(saveCompleteAction == true) {
                pd.hide();
                saveCompleteAction = false;
            }

            if(readFileComplete == true) {

                readFileComplete = false;
                tv.setText(Maintext);
                pd.hide();

            }
        }
    };




    public void generateNumbers(){
        //TextView txtView = (TextView) findViewById(R.id.scrText);

        Maintext = "10,000 random 32 bit float generated.\n\n";

        inGeneration = true;


        Random r = new Random();

        i = 0;

        float max =Float.MAX_VALUE;
        float min =Float.MIN_VALUE;
        // Exit when x becomes greater than 4
        while (i < 10000)
        {
            theData[i] = r.nextFloat()* (max - min) + min;
            Maintext += Float.toString(theData[i]) + " \n";
            //increment the value of x for next iteration
            i++;
        }

        inGeneration = false;
        activityCompleteRefresh = true;
        initialStateComplete = true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


//        TextView txtView = (TextView) findViewById(R.id.scrText);
  //      txtView.setText("Hello 2 " + item.toString());


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_generate) {

            tv.setText("...");

            startGenerate();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }






    private void stringToFile( String text, String fileName )
    {
        try
        {

            FileOutputStream fos = openFileOutput(fileName, Context.MODE_PRIVATE);

            fos.write(text.getBytes());
            fos.close();



        }
        catch( IOException e )
        {
            System.out.println("Error: " + e);
            e.printStackTrace( );


        }


        saveCompleteAction = true;
    }


    private void readStringFile(String fileName )
    {
        try
        {

            //FileInputStream fos = openFileInput(fileName);

            //fos.read(Maintext.getBytes());
            //fos.close();

            readingTheFile = true;
            i = 0;

            FileInputStream fis = openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            //StringBuilder sb = new StringBuilder();
            String line;
            Maintext = "10,000 random 32 bit float generated.\n\n";

            int loop = 0;
            while ((line = bufferedReader.readLine()) != null) {
                if(loop < 2){
                    loop ++;

                }
                else{
                    if(i < 10000) {
                        theData[i] = Float.parseFloat(line);
                    }
                    Maintext += line +"\n";
                    i++;
                }
                //sb.append(line);


            }


            readingTheFile = false;


        }
        catch( IOException e )
        {
            System.out.println("Error: " + e);
            e.printStackTrace( );


        }


        readFileComplete = true;
    }

    public boolean fileExist(String fname){
        File file = getBaseContext().getFileStreamPath(fname);
        return file.exists();
    }
}
