package DnD.DiceRoller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    public Dice[] dices = new Dice[]{
            new Dice(20),
            new Dice(12),
            new Dice(10),
            new Dice(8),
            new Dice(6),
            new Dice(4),
    };
    LinearLayout mainLayout;
    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    LinearLayout.LayoutParams viewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
    LinearLayout[] diceLayouts = new LinearLayout[dices.length];
    TextView[] resultViews = new TextView[dices.length];
    TextView sumView;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainLayout = new LinearLayout(this);
        mainLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mainLayout.setOrientation(LinearLayout.VERTICAL);

        setContentView(mainLayout);
        Context context = mainLayout.getContext();
        String[] texts = new String[]{"d20", "d12", "d10", "d8", "d6", "d4"};
        for (int i = 0; i < texts.length; ++i) {
            LinearLayout layout = createDiceLayout(context, texts[i]);
            diceLayouts[i] = layout;
            mainLayout.addView(layout);
        }

        Button button = new Button(context);
        button.setText("Roll");
        button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        button.setOnClickListener(view -> {
            int sum = 0;
            for (int i = 0; i < diceLayouts.length; ++i) {
                LinearLayout layout = diceLayouts[i];
                EditText editText = (EditText) layout.getChildAt(2);
                int d = editText.getText().toString().trim().length() == 0 ?
                        0 : Integer.parseInt(editText.getText().toString());
                int[] rolls = dices[i].roll(d);
                int tmp = Arrays.stream(rolls).sum();
                sum += tmp;
                resultViews[i].setText(dices[i].getName() + ": " + Arrays.toString(rolls) + " = " + tmp);
            }
            sumView.setText(String.valueOf(sum));
        });
        mainLayout.addView(button);

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
        sumView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        sumView.setTextColor(Color.YELLOW);
        sumView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        sumView.setTextAppearance(android.R.style.TextAppearance_Large);
        mainLayout.addView(sumView);

        Button btnReset = new Button(context);
        btnReset.setText("Reset");
        btnReset.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        btnReset.setOnClickListener(view -> {
            for (LinearLayout layout : diceLayouts) {
                EditText editText = (EditText) layout.getChildAt(2);
                editText.setText("0");
            }
            for (TextView textView : resultViews)
                textView.setText("");
            sumView.setText("");
        });
        mainLayout.addView(btnReset);
    }

    @SuppressLint("ClickableViewAccessibility")
    private LinearLayout createDiceLayout(Context parentContext, String text) {
        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

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
        buttonMinus.setLayoutParams(btnParams);
        buttonMinus.setOnClickListener(view -> {
            LinearLayout layout1 = (LinearLayout) view.getParent();
            EditText editText = (EditText) layout1.getChildAt(2);
            if (editText.getText().toString().trim().length() == 0) {
                editText.setText("1");
                return;
            }
            editText.setText(String.valueOf(Math.max(Integer.parseInt(editText.getText().toString()) - 1, 0)));
        });
        buttonMinus.setOnTouchListener(new RepeatListener(400, 100, view -> {
            LinearLayout layout12 = (LinearLayout) view.getParent();
            EditText editText = (EditText) layout12.getChildAt(2);
            if (editText.getText().toString().trim().length() == 0) {
                editText.setText("1");
                return;
            }
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
        buttonPlus.setLayoutParams(btnParams);
        buttonPlus.setOnClickListener(view -> {
            LinearLayout layout13 = (LinearLayout) view.getParent();
            EditText editText1 = (EditText) layout13.getChildAt(2);
            if (editText1.getText().toString().trim().length() == 0) {
                editText1.setText("1");
                return;
            }
            editText1.setText(String.valueOf(Integer.parseInt(editText1.getText().toString()) + 1));
        });
        buttonPlus.setOnTouchListener(new RepeatListener(400, 100, view -> {
            LinearLayout layout14 = (LinearLayout) view.getParent();
            EditText editText12 = (EditText) layout14.getChildAt(2);
            if (editText12.getText().toString().trim().length() == 0) {
                editText12.setText("1");
                return;
            }
            editText12.setText(String.valueOf(Integer.parseInt(editText12.getText().toString()) + 1));
        }));
        layout.addView(buttonPlus);

        return layout;
    }

}