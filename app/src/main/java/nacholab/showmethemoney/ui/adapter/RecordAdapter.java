package nacholab.showmethemoney.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import nacholab.showmethemoney.model.MoneyRecord;
import nacholab.showmethemoney.ui.view.RecordView;

public class RecordAdapter extends RecyclerView.Adapter{

    private final RecordView.Listener listener;
    private List<MoneyRecord> records;

    public RecordAdapter(RecordView.Listener _listener) {
        records = new ArrayList<>();
        listener = _listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecordView v = new RecordView(parent.getContext());
        v.setListener(listener);
        return new RecordViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((RecordViewHolder)holder).v.setRecord(records.get(position));
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    public void clear(){
        records.clear();
    }

    public void add(List<MoneyRecord> r){
        records.addAll(r);
    }

    public void remove(MoneyRecord r) {
        records.remove(r);
    }

    private class RecordViewHolder extends RecyclerView.ViewHolder{
        RecordView v;

        public RecordViewHolder(RecordView _v) {
            super(_v);
            v = _v;
        }
    }

}
