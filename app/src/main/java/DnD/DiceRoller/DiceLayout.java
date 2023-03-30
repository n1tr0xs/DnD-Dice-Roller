package DnD.DiceRoller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("ViewConstructor")
public class DiceLayout extends LinearLayout {
    TextView textView;
    Button buttonMinus, buttonPlus;
    EditText editText;
    CheckBox checkAdv, checkDis;
    LinearLayout.LayoutParams
            layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT),
            viewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1),

    diceLayoutBtnParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT),
            diceLayoutCheckParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    public DiceLayout(Context context, String text) {
        super(context);
        setOrientation(LinearLayout.HORIZONTAL);
        setLayoutParams(layoutParams);
        setPadding(0, 10, 30, 10);
        Context layoutContext = this.getContext();

        textView = new TextView(layoutContext);
        textView.setText(text);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setLayoutParams(new LayoutParams(100, ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(textView);


        buttonMinus = new Button(layoutContext);
        buttonMinus.setText("-");
        buttonMinus.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        buttonMinus.setOnClickListener(view -> {
            if (editText.getText().toString().trim().length() == 0)
                editText.setText("1");
            else
                editText.setText(String.valueOf(Math.max(Integer.parseInt(editText.getText().toString()) - 1, 0)));
        });
        buttonMinus.setOnTouchListener(new RepeatListener(400, 100, view -> {
            if (editText.getText().toString().trim().length() == 0)
                editText.setText("1");
            else
                editText.setText(String.valueOf(Math.max(Integer.parseInt(editText.getText().toString()) - 1, 0)));
        }));
        addView(buttonMinus);

        editText = new EditText(layoutContext);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setText("0");
        editText.setLayoutParams(viewParams);
        editText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        editText.setMaxLines(1);
        editText.setOnClickListener(view -> editText.setSelection(0, editText.getText().toString().length()));
        addView(editText);

        buttonPlus = new Button(layoutContext);
        buttonPlus.setText("+");
        buttonPlus.setLayoutParams(diceLayoutBtnParams);
        buttonPlus.setOnClickListener(view -> {
            if (editText.getText().toString().trim().length() == 0)
                editText.setText("1");
            else
                editText.setText(String.valueOf(Integer.parseInt(editText.getText().toString()) + 1));
        });
        buttonPlus.setOnTouchListener(new RepeatListener(400, 100, view -> {
            if (editText.getText().toString().trim().length() == 0)
                editText.setText("1");
            else
                editText.setText(String.valueOf(Integer.parseInt(editText.getText().toString()) + 1));
        }));
        addView(buttonPlus);


        checkAdv = new CheckBox(layoutContext);
        checkAdv.setText("Adv");
        checkAdv.setLayoutParams(diceLayoutCheckParams);
        addView(checkAdv);

        checkDis = new CheckBox(layoutContext);
        checkDis.setText("Dis");
        checkDis.setLayoutParams(diceLayoutCheckParams);
        addView(checkDis);

        checkAdv.setOnCheckedChangeListener((compoundButton, b) -> {
            if (checkAdv.isChecked() && checkDis.isChecked())
                checkDis.setChecked(false);
        });
        checkDis.setOnCheckedChangeListener((compoundButton, b) -> {
            if (checkDis.isChecked() && checkAdv.isChecked())
                checkAdv.setChecked(false);
        });
    }
}
