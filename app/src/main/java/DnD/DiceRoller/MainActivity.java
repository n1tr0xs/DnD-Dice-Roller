package DnD.DiceRoller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    public Dice[] dices = {
            new Dice(20),
            new Dice(12),
            new Dice(10),
            new Dice(8),
            new Dice(6),
            new Dice(4),
    };
    int EDIT_TEXT_INDEX = 2;
    int ADV_CHECK_INDEX = 4;
    int DIS_CHECK_INDEX = 5;
    String[] texts = {
            "d20",
            "d12",
            "d10",
            "d8",
            "d6",
            "d4"
    };
    LinearLayout mainLayout;
    LinearLayout[] diceLayouts = new LinearLayout[dices.length];
    TextView[] resultViews = new TextView[dices.length];
    TextView sumView;
    LinearLayout.LayoutParams
            layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT),
            viewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1),
            mainLayoutBtnParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT),
            diceLayoutBtnParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT),
            diceLayoutCheckParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainLayout = new LinearLayout(this);
        mainLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        setContentView(mainLayout);

        Context context = mainLayout.getContext();
        for (int i = 0; i < texts.length; ++i) {
            LinearLayout layout = createDiceLayout(context, texts[i]);
            diceLayouts[i] = layout;
            mainLayout.addView(layout);
        }

        Button buttonRoll = new Button(context);
        buttonRoll.setText("Roll");
        buttonRoll.setLayoutParams(mainLayoutBtnParams);
        buttonRoll.setOnClickListener(view -> {
            int sum = 0;
            for (int i = 0; i < diceLayouts.length; ++i) {
                LinearLayout layout = diceLayouts[i];
                EditText editText = (EditText) layout.getChildAt(EDIT_TEXT_INDEX);
                CheckBox
                        checkAdv = (CheckBox) layout.getChildAt(ADV_CHECK_INDEX),
                        checkDis = (CheckBox) layout.getChildAt(DIS_CHECK_INDEX);

                int d = (editText.getText().toString().trim().length() == 0) ? 0 : Integer.parseInt(editText.getText().toString());
                int[] rolls = dices[i].roll(d, checkAdv.isChecked(), checkDis.isChecked());
                int tmp = Arrays.stream(rolls).sum();
                sum += tmp;
                resultViews[i].setText(dices[i].getName() + ": " + Arrays.toString(rolls) + " = " + tmp);
            }
            sumView.setText(String.valueOf(sum));
        });
        mainLayout.addView(buttonRoll);

        for (int i = 0; i < texts.length; ++i) {
            TextView resultView = new TextView(context);
            resultView.setText("");
            resultView.setLayoutParams(viewParams);
            resultView.setTextColor(Color.GREEN);
            resultViews[i] = resultView;
            mainLayout.addView(resultView);
        }

        sumView = new TextView(context);
        sumView.setText("");
        sumView.setLayoutParams(viewParams);
        sumView.setTextColor(Color.YELLOW);
        sumView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        sumView.setTextAppearance(android.R.style.TextAppearance_Large);
        mainLayout.addView(sumView);

        Button btnReset = new Button(context);
        btnReset.setText("Reset");
        btnReset.setLayoutParams(mainLayoutBtnParams);
        btnReset.setOnLongClickListener(view -> {
            for (int i = 0; i < diceLayouts.length; ++i) {
                LinearLayout layout = diceLayouts[i];
                EditText editText = (EditText) layout.getChildAt(EDIT_TEXT_INDEX);
                CheckBox
                        checkAdv = (CheckBox) layout.getChildAt(ADV_CHECK_INDEX),
                        checkDis = (CheckBox) layout.getChildAt(DIS_CHECK_INDEX);
                editText.setText("0");
                checkAdv.setChecked(false);
                checkDis.setChecked(false);
                resultViews[i].setText("");
            }
            sumView.setText("");
            return true;
        });
        mainLayout.addView(btnReset);
    }

    @NonNull
    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    private LinearLayout createDiceLayout(Context parentContext, String text) {
        LinearLayout layout = new LinearLayout(parentContext);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setLayoutParams(layoutParams);
        Context context = layout.getContext();

        TextView textView = new TextView(context);
        textView.setText(text);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setLayoutParams(new LinearLayout.LayoutParams(100, ViewGroup.LayoutParams.WRAP_CONTENT));
        layout.addView(textView);

        Button buttonMinus = new Button(context);
        buttonMinus.setText("-");
        buttonMinus.setLayoutParams(diceLayoutBtnParams);
        buttonMinus.setOnClickListener(view -> {
            EditText editText = (EditText) layout.getChildAt(EDIT_TEXT_INDEX);
            if (editText.getText().toString().trim().length() == 0)
                editText.setText("1");
            else
                editText.setText(String.valueOf(Math.max(Integer.parseInt(editText.getText().toString()) - 1, 0)));
        });
        buttonMinus.setOnTouchListener(new RepeatListener(400, 100, view -> {
            EditText editText = (EditText) layout.getChildAt(EDIT_TEXT_INDEX);
            if (editText.getText().toString().trim().length() == 0)
                editText.setText("1");
            else
                editText.setText(String.valueOf(Math.max(Integer.parseInt(editText.getText().toString()) - 1, 0)));
        }));
        layout.addView(buttonMinus);

        EditText editText = new EditText(context);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setText("0");
        editText.setLayoutParams(viewParams);
        editText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        editText.setMaxLines(1);
        editText.setOnClickListener(view -> {
            EditText e = (EditText) view;
            e.setSelection(0, e.getText().toString().length());
        });
        layout.addView(editText);

        Button buttonPlus = new Button(context);
        buttonPlus.setText("+");
        buttonPlus.setLayoutParams(diceLayoutBtnParams);
        buttonPlus.setOnClickListener(view -> {
            EditText editText1 = (EditText) layout.getChildAt(EDIT_TEXT_INDEX);
            if (editText1.getText().toString().trim().length() == 0)
                editText1.setText("1");
            else
                editText1.setText(String.valueOf(Integer.parseInt(editText1.getText().toString()) + 1));
        });
        buttonPlus.setOnTouchListener(new RepeatListener(400, 100, view -> {
            EditText editText12 = (EditText) layout.getChildAt(EDIT_TEXT_INDEX);
            if (editText12.getText().toString().trim().length() == 0)
                editText12.setText("1");
            else
                editText12.setText(String.valueOf(Integer.parseInt(editText12.getText().toString()) + 1));
        }));
        layout.addView(buttonPlus);

        CheckBox checkAdv = new CheckBox(context);
        checkAdv.setText("Adv");
        checkAdv.setLayoutParams(diceLayoutCheckParams);
        layout.addView(checkAdv);

        CheckBox checkDis = new CheckBox(context);
        checkDis.setText("Dis");
        checkDis.setLayoutParams(diceLayoutCheckParams);
        layout.addView(checkDis);

        checkAdv.setOnCheckedChangeListener((compoundButton, b) -> {
            if (checkAdv.isChecked())
                if (checkDis.isChecked())
                    checkDis.setChecked(false);
        });
        checkDis.setOnCheckedChangeListener((compoundButton, b) -> {
            if (checkDis.isChecked())
                if (checkAdv.isChecked())
                    checkAdv.setChecked(false);
        });

        return layout;
    }

}