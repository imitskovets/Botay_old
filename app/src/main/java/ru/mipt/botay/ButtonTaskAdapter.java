package ru.mipt.botay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

public class ButtonTaskAdapter extends BaseAdapter {

    public Integer[] mSubjectProgress;
    public Context mContext;
    public String[] mTaskName;

    public ButtonTaskAdapter(Context c,Integer[] mSubjectProgress,String[] mTaskName) {
        this.mContext = c;
        this.mSubjectProgress = mSubjectProgress;
        this.mTaskName = mTaskName;
    }

    /*public static Integer[] toObject(int[] intArray) {
        Integer[] result = new Integer[intArray.length];
        for (int i = 0; i < intArray.length; i++) {
            result[i] = Integer.valueOf(intArray[i]);
        }
        return result;
    }*/

    public int getCount() {
        return mTaskName.length;
    }

    public Object getItem(int position) {
        return mTaskName[position];
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;
        if (convertView == null) {
            grid = new View(mContext);
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            grid = inflater.inflate(R.layout.cellgrid, parent, false);
        } else {
            grid = (View) convertView;
        }

        Button buttonView = (Button) grid.findViewById(R.id.button) ;
        buttonView.setText(mTaskName[position]);
        if (mSubjectProgress[position] == 0 ){
            buttonView.setTextColor(0xffe53096);//red
        }
        else{
            buttonView.setTextColor(0xFF0A8F00);
        }
        return grid;
    }

}