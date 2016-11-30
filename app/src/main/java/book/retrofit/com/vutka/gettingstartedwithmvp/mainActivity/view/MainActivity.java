package book.retrofit.com.vutka.gettingstartedwithmvp.mainActivity.view;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import book.retrofit.com.vutka.gettingstartedwithmvp.R;
import book.retrofit.com.vutka.gettingstartedwithmvp.mainActivity.MainMVP;
import book.retrofit.com.vutka.gettingstartedwithmvp.mainActivity.common.StateMaintainer;
import book.retrofit.com.vutka.gettingstartedwithmvp.mainActivity.model.MainModel;
import book.retrofit.com.vutka.gettingstartedwithmvp.mainActivity.presenter.MainPresenter;
import book.retrofit.com.vutka.gettingstartedwithmvp.mainActivity.view.recycler.NotesViewHolder;

public class MainActivity extends AppCompatActivity implements View.OnClickListener , MainMVP.RequiredViewOps{

    private EditText mTextNewNote;
    private NotesAdapter mNoteAdapter;
    private ProgressBar mProgress;

    private MainMVP.ProvidedPresenterOps mPresenter;


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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy(isChangingConfigurations());
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

            // Create the Model
            MainModel model = new MainModel(presenter);

            //set presenter model
            presenter.setModel(model);

            // Add Presenter and Model to StateMaintainer
            mStateMaintainer.put(presenter);
            mStateMaintainer.put(model);

            // Set the Presenter as a interface
            // To limit the communication with it
            mPresenter = presenter;
        }
    }


    /**
     * Setup the Views
     */

    private void setUpViews() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        mTextNewNote = (EditText) findViewById(R.id.edit_note);
        mNoteAdapter = new NotesAdapter();
        mProgress = (ProgressBar) findViewById(R.id.progressbar);

        RecyclerView mList = (RecyclerView) findViewById(R.id.list_notes);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( this );
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mList.setLayoutManager(linearLayoutManager);
        mList.setAdapter(mNoteAdapter);
        mList.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.fab){
            mPresenter.clickNewNote(mTextNewNote);
        }
    }

    @Override
    public Context getActivityContext() {
        return this;
    }



    @Override
    public void notifyItemInserted(int layoutPosition) {
        mNoteAdapter.notifyItemInserted(layoutPosition);
    }

    @Override
    public void notifyitemRemoved(int adapterPos) {
        mNoteAdapter.notifyItemRemoved(adapterPos);
    }

    @Override
    public void notifyItemRangeChanged(int positionStat, int itemCount) {
        mNoteAdapter.notifyItemRangeChanged(positionStat , itemCount);
    }

    @Override
    public void notifyDataSetChanged() {
        mNoteAdapter.notifyDataSetChanged();
    }

    @Override
    public void clearEditText() {
        mTextNewNote.setText("");
    }

    @Override
    public void showProgress() {
        mProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mProgress.setVisibility(View.GONE);
    }

    @Override
    public void showAlert(AlertDialog alertDialog) {
        alertDialog.show();
    }

    @Override
    public void showToast(Toast toast) {
        toast.show();
    }


    /**
     * Adapter class for notes
     */

    public class NotesAdapter extends RecyclerView.Adapter<NotesViewHolder>{


        @Override
        public NotesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return mPresenter.createViewHolder(parent , viewType);
        }

        @Override
        public void onBindViewHolder(NotesViewHolder holder, int position) {
            mPresenter.bindViewHolder(holder , position);
        }

        @Override
        public int getItemCount() {
            return mPresenter.getItemCount();
        }
    }


}
