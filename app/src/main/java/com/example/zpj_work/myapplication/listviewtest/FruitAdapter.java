package com.example.zpj_work.myapplication.listviewtest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.zpj_work.myapplication.R;

import java.util.List;

/**
 * @创建者 zhengpengjie
 * @创建日期 2019/9/30 0030 14:17
 * @描述
 */
public class FruitAdapter extends ArrayAdapter<Fruit> {

    private  int resoureId;

    //重写父类的一组构造函数，将上下文，ListView子项布局的id和数据都传递进来
    public FruitAdapter(Context context, int textViewResourceId, List<Fruit> objects){
        super(context,textViewResourceId,objects);
        resoureId=textViewResourceId;

    }

    //重写getView()，在每个子项被滚动到屏幕内的时候会被调用
    public View getView(int position, View convertView, ViewGroup parent){
        //获取当前项的 Fruit 实例
        Fruit fruit=getItem(position);
        //
        //使用 LayoutInflater 来为这个子项加载我们传入的布局
        View view = LayoutInflater.from(getContext()).inflate(resoureId,parent,false);
        //获取TextView实例
        TextView fruitName=(TextView)view.findViewById(R.id.fruit_number);
        TextView fruitAntennas=(TextView)view.findViewById(R.id.fruit_antennas);
        TextView fruitDataepc=(TextView)view.findViewById(R.id.fruit_dataepc);
        TextView fruitDatatid=(TextView)view.findViewById(R.id.fruit_datatid);
        TextView fruitDatauser=(TextView)view.findViewById(R.id.fruit_datauser);

        //调用setText()设置显示的Number和Data
        fruitName.setText(fruit.getNumber()+" ");
        fruitAntennas.setText(fruit.getAntennas()+" ");
        fruitDataepc.setText(fruit.getDataepc());
        fruitDatatid.setText(fruit.getDatatid());
        fruitDatauser.setText(fruit.getDatauser());

        //返回布局
        return view;


    }
}
