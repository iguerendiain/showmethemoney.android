package nacholab.showmethemoney.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.tokenautocomplete.TokenCompleteTextView;

public class TagsInputView extends TokenCompleteTextView<String> {

    public TagsInputView(Context context) {
        super(context);
    }

    public TagsInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TagsInputView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected View getViewForObject(String object) {
        TagView view = new TagView(getContext());
        view.setText(object);
        return view;
    }

    @Override
    protected String defaultObject(String completionText) {
        return completionText;
    }
}
