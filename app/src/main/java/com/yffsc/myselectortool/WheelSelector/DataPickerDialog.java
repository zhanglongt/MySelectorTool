package com.yffsc.myselectortool.WheelSelector;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.yffsc.myselectortool.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataPickerDialog extends Dialog {

    private Params params;

    public DataPickerDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    private void setParams(DataPickerDialog.Params params) {
        this.params = params;
    }


    public interface OnDataSelectedListener {
        void onDataSelected(String itemKey1, String itemValue1, int itemId1,String itemKey2, String itemValue2, int itemId2);
    }

    private static final class Params {
        private boolean shadow = true;
        private boolean canCancel = true;
        private LoopView loopData1, loopData2;
        private String title;
        private String unit;
        private int initSelection1,initSelection2;
        private boolean isCycle;
        private OnDataSelectedListener callback;
        private final List<String> dataList1 = new ArrayList<>();
        private final List<String> dataList2 = new ArrayList<>();
        private final List<Map<String, String>> mapDataList = new ArrayList<>();

    }

    public static class Builder {
        private final Context context;
        private final DataPickerDialog.Params params;

        public Builder(Context context) {
            this.context = context;
            params = new DataPickerDialog.Params();
        }

        private final String getCurrDateValue1() {
            return params.loopData1.getCurrentItemValue();
        }

        private final int getCurrDateValueId1() {
            return params.loopData1.getCurrentItem();
        }

        private final String getCurrDateValue2() {
            return params.loopData2.getCurrentItemValue();
        }

        private final int getCurrDateValueId2() {
            return params.loopData2.getCurrentItem();
        }


        public Builder setData(List<String> dataList) {
            params.dataList1.clear();
            params.dataList1.addAll(dataList);
            return this;
        }

        private List<Map<String, String>> mapData, mapData2;
        private List<String> dataL, dataL2;

        public Builder setMapData(List<Map<String, String>> mapDataList1, List<Map<String, String>> mapDataList2) {
            dataL = new ArrayList<>();
            mapData = mapDataList1;
            for (int i = 0; i < mapDataList1.size(); i++) {
                dataL.add(mapDataList1.get(i).entrySet().iterator().next().getValue());
            }
            params.dataList1.clear();
            params.dataList1.addAll(dataL);
            if (mapDataList2 != null) {
                dataL2 = new ArrayList<>();
                mapData2 = mapDataList2;
                for (int i = 0; i < mapDataList2.size(); i++) {
                    dataL2.add(mapDataList2.get(i).entrySet().iterator().next().getValue());
                }
                params.dataList2.clear();
                params.dataList2.addAll(dataL2);
            }
            return this;
        }

        public Builder setTitle(String title) {
            params.title = title;
            return this;
        }

        public Builder setUnit(String unit) {
            params.unit = unit;
            return this;
        }

        public final String getText_key1() {//获取键1
            return mapData.get(getCurrDateValueId1()).entrySet().iterator().next().getKey();
        }

        public final String getText_key2() {//获取键2
            if(mapData2!=null) {
                return mapData2.get(getCurrDateValueId2()).entrySet().iterator().next().getKey();
            }else {
                return "";
            }
        }


//    public String getText_value(){//获取值
//        return mapData.get(getCurrDateValueId()).entrySet().iterator().next().getValue();
//    }


        public Builder setSelection(int selection,int selection2) {
            params.initSelection1 = selection;
            params.initSelection2 = selection2;
            return this;
        }

        public Builder setSelection(String itemValue1,String itemValue2) {
            if (params.dataList1.size() > 0) {
                int idx = params.dataList1.indexOf(itemValue1);
                if (idx >= 0) {
                    params.initSelection1 = idx;
                    //params.loopData1.setCurrentItem(params.initSelection1);
                }
            }
            if (params.dataList2.size() > 0) {
                int idx = params.dataList2.indexOf(itemValue2);
                if (idx >= 0) {
                    params.initSelection2 = idx;
                    //params.loopData2.setCurrentItem(params.initSelection2);
                }
            }
            return this;

        }

        public Builder setCyclen(boolean isCycle) {
            params.isCycle = isCycle;
            return this;
        }


        public Builder setOnDataSelectedListener(OnDataSelectedListener onDataSelectedListener) {
            params.callback = onDataSelectedListener;
            return this;
        }


        public DataPickerDialog create() {
            final DataPickerDialog dialog = new DataPickerDialog(context, params.shadow ? R.style.Theme_Light_NoTitle_Dialog : R.style.Theme_Light_NoTitle_NoShadow_Dialog);
            View view = LayoutInflater.from(context).inflate(R.layout.layout_picker_data, null);

            if (!TextUtils.isEmpty(params.title)) {
                TextView txTitle = (TextView) view.findViewById(R.id.tx_title);
                txTitle.setText(params.title);
            }
            if (!TextUtils.isEmpty(params.unit)) {
                TextView txUnit = (TextView) view.findViewById(R.id.tx_unit);
                txUnit.setText(params.unit);
            }

            final LoopView loopData1 = (LoopView) view.findViewById(R.id.loop_data);
            loopData1.setArrayList(params.dataList1);
            loopData1.setNotLoop();
            if (params.dataList1.size() > 0) {
                loopData1.setCurrentItem(params.initSelection1);
                loopData1.setCyclic(params.isCycle);
            }

            final LoopView loopData2 = (LoopView) view.findViewById(R.id.loop_data2);
            if(params.dataList2.size()>0) {
                view.findViewById(R.id.flPick2).setVisibility(View.VISIBLE);
                loopData2.setArrayList(params.dataList2);
                loopData2.setNotLoop();
                loopData2.setCurrentItem(params.initSelection2);
                loopData2.setCyclic(params.isCycle);
            }

            view.findViewById(R.id.tx_finish).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    params.callback.onDataSelected(getText_key1(), getCurrDateValue1(), getCurrDateValueId1()
                                                    ,getText_key2(),getCurrDateValue2(),getCurrDateValueId2());
                }
            });
            view.findViewById(R.id.tx_title).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            Window win = dialog.getWindow();
            win.getDecorView().setPadding(0, 0, 0, 0);
            WindowManager.LayoutParams lp = win.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            win.setAttributes(lp);
            win.setGravity(Gravity.BOTTOM);
            win.setWindowAnimations(R.style.Animation_Bottom_Rising);

            dialog.setContentView(view);
            dialog.setCanceledOnTouchOutside(params.canCancel);
            dialog.setCancelable(params.canCancel);

            params.loopData1 = loopData1;
            params.loopData2 = loopData2;
            dialog.setParams(params);

            return dialog;
        }
    }
}
