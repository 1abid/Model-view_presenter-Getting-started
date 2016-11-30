package book.retrofit.com.vutka.gettingstartedwithmvp.mainActivity.view;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import book.retrofit.com.vutka.gettingstartedwithmvp.R;
import book.retrofit.com.vutka.gettingstartedwithmvp.mainActivity.MainMVP;
import book.retrofit.com.vutka.gettingstartedwithmvp.mainActivity.common.StateMaintainer;
import book.retrofit.com.vutka.gettingstartedwithmvp.mainActivity.presenter.MainPresenter;
import book.retrofit.com.vutka.gettingstartedwithmvp.mainActivity.view.adapter.NotesAdapter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener , MainMVP.RequiredViewOps{

    private EditText mTextNewNote;
    private NotesAdapter mNoteAdapter;
    private ProgressBar mProgress;

    private MainMVP.ProvidedPresenterOps mPrester;


    // Responsible to maintain the object's integrity
    // during configurations change
    private final StateMaintainer mStateMaintainer
            = new StateMaintainer(MainActivity.class.getName() , getFragmentManager());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        
        setUpViews();
        setUpMvp();
    }




    /**
     * Setup Model View Presenter pattern.
     * Use a {@link StateMaintainer} to maintain the
     * Presenter and Model instances between configuration changes.
     * Could be done differently,
     * using a dependency injection for example.
     */

    private void setUpMvp() {

        //check if statemainer has been created
        if(mStateMaintainer.firstTimeIn()){
            //create the presenter
            MainPresenter presenter = new MainPresenter(this);
        }
    }


    /**
     * Setup the Views
     */

    private void setUpViews() {
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public Context getActivityContext() {
        return null;
    }

    @Override
    public void notifyItemInserted(int layoutPosition) {

    }

    @Override
    public void notifyitemRemoved(int adapterPos) {

    }

    @Override
    public void notifyItemRangeChanged(int positionStat, int itemCount) {

    }

    @Override
    public void notifyDataSetChanged() {

    }

    @Override
    public void clearEditText() {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showAlert(AlertDialog alertDialog) {

    }

    @Override
    public void showToast(Toast toast) {

    }
}
