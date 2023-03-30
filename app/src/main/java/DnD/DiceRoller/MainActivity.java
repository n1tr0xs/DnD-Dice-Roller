package DnD.DiceRoller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

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
            mainLayoutBtnParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT),
            viewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // main layout creating
        mainLayout = new LinearLayout(this);
        mainLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        setContentView(mainLayout);
        Context mainLayoutContext = mainLayout.getContext();

        // dice layouts creating
        for (int i = 0; i < texts.length; ++i) {
            LinearLayout layout = new DiceLayout(mainLayoutContext, texts[i]);
            diceLayouts[i] = layout;
            mainLayout.addView(layout);
        }

        // roll button creating
        Button buttonRoll = new Button(mainLayoutContext);
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
                String info = "";
                if (checkAdv.isChecked())
                    info = " adv";
                else if (checkDis.isChecked())
                    info = " dis";
                resultViews[i].setText(dices[i].getName() + info + ": " + Arrays.toString(rolls) + " = " + tmp);
            }
            sumView.setText(String.valueOf(sum));
        });
        mainLayout.addView(buttonRoll);

        // result textviews creating
        for (int i = 0; i < texts.length; ++i) {
            TextView resultView = new TextView(mainLayoutContext);
            resultView.setText("");
            resultView.setLayoutParams(viewParams);
            resultView.setTextColor(Color.GREEN);
            resultViews[i] = resultView;
            mainLayout.addView(resultView);
        }

        // sum textview creating
        sumView = new TextView(mainLayoutContext);
        sumView.setText("");
        sumView.setLayoutParams(viewParams);
        sumView.setTextColor(Color.YELLOW);
        sumView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        sumView.setTextAppearance(android.R.style.TextAppearance_Large);
        mainLayout.addView(sumView);

        // reset button creating
        Button btnReset = new Button(mainLayoutContext);
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
}