package book.retrofit.com.vutka.gettingstartedwithmvp.mainActivity.common;


import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * Created by vutka bilai on 11/30/16.
 * mail : la4508@gmail.com
 * <p>
 * <p>
 * this class Retains and maintain object's state between configuration changes
 * in Activitys and Fragments
 */

public class StateMaintainer {

    protected final String TAG = getClass().getSimpleName();

    private final String mStateMainterTag;
    private final WeakReference<FragmentManager> mFragmentManager;
    private StateMngFragment mStatemainterFrag;
    private boolean isRecreating ;


    public StateMaintainer(String mStateMainterTag, FragmentManager mFragmentManager) {
        this.mStateMainterTag = mStateMainterTag;
        this.mFragmentManager = new WeakReference<FragmentManager>(mFragmentManager);
    }


    /**
     * Creates the Fragment responsible to maintain the objects.
     * @return  true: fragment just created
     */
    public boolean firstTimeIn(){

        try {
            mStatemainterFrag = (StateMngFragment) mFragmentManager.get().findFragmentByTag(mStateMainterTag);


            if (mStatemainterFrag == null) {
                Log.d(TAG, "no saved fragment found to retainted " + mStatemainterFrag);
                mStatemainterFrag = new StateMngFragment();
                mFragmentManager.get().beginTransaction().add(mStatemainterFrag, mStateMainterTag).commit();
                isRecreating = false;

                return true;
            } else {
                Log.d(TAG, "saved fragment found , retaining fragment " + mStatemainterFrag);
                isRecreating = true;

                return false;
            }
        }catch (NullPointerException e){
            Log.e(TAG , " error "+e.getMessage());
            return false;
        }


    }



    /**
     * Return <strong>true</strong> it the current Activity was recreated at least one time
     * @return  If the Activity was recreated
     */
    public boolean wasRecreated() { return isRecreating; }



    /**
     * Insert the object to be preserved.
     * @param key   object's TAG
     * @param obj   object to maintain
     */
    public void put(String key, Object obj) {
        mStatemainterFrag.put(key, obj);
    }

    /**
     * Insert the object to be preserved.
     * @param obj   object to maintain
     */
    public void put(Object obj) {
        put(obj.getClass().getName(), obj);
    }


    /**
     * Recovers the object saved
     * @param key   Object's TAG
     * @param <T>   Object type
     * @return      Object saved
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key)  {
        return mStatemainterFrag.get(key);

    }

    /**
     * Checks the existence of a given object
     * @param key   Key to verification
     * @return      true: Object exists
     */
    public boolean hasKey(String key) {
        return mStatemainterFrag.get(key) != null;
    }



    /**
     * Fragment resposible to preserve objects.
     * Instantiated only once. Uses a hashmap to save objs
     */
    public static class StateMngFragment extends Fragment{

        private HashMap<String , Object> mData = new HashMap<>();

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Grants that the fragment will be preserved
            setRetainInstance(true);
        }



        /**
         * Insert objects on the hashmap
         * @param key   Reference key
         * @param obj   Object to be saved
         */
        public void put(String key , Object obj){
            mData.put(key , obj);
        }


        /**
         * Insert objects on the hashmap
         * @param object    Object to be saved
         */
        public void put(Object object) {
            put(object.getClass().getName(), object);
        }


        /**
         * Recovers saved object
         * @param key   Reference key
         * @param <T>   Object type
         * @return      Object saved
         */
        @SuppressWarnings("unchecked")
        public <T> T get(String key) {
            return (T) mData.get(key);
        }
    }
}
