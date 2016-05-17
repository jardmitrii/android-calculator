package com.example.djar.calculator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String MAIN_TEXT = "main_text";
    private static final String HISTORY_TEXT = "history_text";
    private String main_text = "";
    private String history_text = "";
    private String error_text = "";

    private void setOnClicListenerToButtons() {
        // find buttons by id and set them onClickListener
        ((Button) findViewById(R.id.button0)).setOnClickListener(this);
        ((Button) findViewById(R.id.button1)).setOnClickListener(this);
        ((Button) findViewById(R.id.button2)).setOnClickListener(this);
        ((Button) findViewById(R.id.button3)).setOnClickListener(this);
        ((Button) findViewById(R.id.button4)).setOnClickListener(this);
        ((Button) findViewById(R.id.button5)).setOnClickListener(this);
        ((Button) findViewById(R.id.button6)).setOnClickListener(this);
        ((Button) findViewById(R.id.button7)).setOnClickListener(this);
        ((Button) findViewById(R.id.button8)).setOnClickListener(this);
        ((Button) findViewById(R.id.button9)).setOnClickListener(this);
        ((Button) findViewById(R.id.button_leftparenthesis)).setOnClickListener(this);
        ((Button) findViewById(R.id.button_rightparenthesis)).setOnClickListener(this);
        ((Button) findViewById(R.id.button_minus)).setOnClickListener(this);
        ((Button) findViewById(R.id.button_plus)).setOnClickListener(this);
        ((Button) findViewById(R.id.button_division)).setOnClickListener(this);
        ((Button) findViewById(R.id.button_multiply)).setOnClickListener(this);
        ((Button) findViewById(R.id.button_point)).setOnClickListener(this);
        ((Button) findViewById(R.id.button_backspace)).setOnClickListener(this);
        ((Button) findViewById(R.id.button_clear)).setOnClickListener(this);
        ((Button) findViewById(R.id.button_equal)).setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setOnClicListenerToButtons();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(MAIN_TEXT, main_text);
        outState.putString(HISTORY_TEXT, history_text);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        main_text = savedInstanceState.getString(MAIN_TEXT, "");
        history_text = savedInstanceState.getString(HISTORY_TEXT, "");
    }

    private void displayText(String str_to_display) {
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(String.valueOf(str_to_display));
    }

    private void addToMainText(String str) {
        main_text += str;
    }

    private void clear() {
        if (!main_text.isEmpty()) {
            main_text = "";
        }
    }

    private void backspace() {
        if (!main_text.isEmpty()) {
            main_text = main_text.substring(0, main_text.length() - 1);
        }
    }

    private void equal() {
        if (!main_text.isEmpty()) {
            main_text = parse_string(main_text);
        }
    }

    private String parse_string(String str_to_parse) {

        // The execution order of math operations
        // expressions in parentheses
        // exponentiation and root extraction
        // multiplying and divisioning
        // addition and substraction
        final String str_leftparenthesis = getString(R.string.str_leftparenthesis);
        final String str_rightparenthesis = getString(R.string.str_rightparenthesis);
        final String str_multiply = getString(R.string.str_multiply);
        final String str_division = getString(R.string.str_division);
        final String str_plus = getString(R.string.str_plus);
        final String str_minus = getString(R.string.str_minus);
        final String str_regexp_parentheses = getString(R.string.str_regexp_parentheses);
        final String str_regexp_multi_div = getString(R.string.str_regexp_multi_div);
        final String str_regexp_add_sub = getString(R.string.str_regexp_add_sub);

        // disclose parentheses
        String[] regex_arr = {str_regexp_parentheses, str_regexp_multi_div, str_regexp_add_sub};

        for (String str_regex: regex_arr) {
            //String str_regex = getString(R.string.str_regexp_parentheses);
            Pattern pattern = Pattern.compile(str_regex);
            Matcher matcher = pattern.matcher(str_to_parse);

            while (matcher.find()) {
                String substr = matcher.group();
                String str_replacement = substr;
                if (str_regex.equals(str_regexp_parentheses)) {
                    str_replacement = parse_string(substr.substring(1, substr.length() - 1));
                } else if (str_regex.equals(str_regexp_multi_div)) {
                    if (substr.indexOf(str_multiply) > -1) {
                        String[] parts = substr.split("\\" + str_multiply);
                        if (parts.length > 1) {
                            Double result = Double.parseDouble(parts[0]) * Double.parseDouble(parts[1]);
                            str_replacement = result.toString();
                        }
                    }
                    if (substr.indexOf(str_division) > -1) {
                        String[] parts = substr.split("\\" + str_division);
                        if (parts.length > 1) {
                            if (Double.parseDouble(parts[1]) != 0) {
                                Double result = Double.parseDouble(parts[0]) / Double.parseDouble(parts[1]);
                                str_replacement = result.toString();
                            } else {
                                show_error_message("Division by zero!");
                            }
                        }
                    }
                } else if (str_regex.equals(str_regexp_add_sub)) {
                    if (substr.indexOf(str_plus) > -1) {
                        String[] parts = substr.split("\\" + str_plus);
                        if (parts.length > 1) {
                            Double result = Double.parseDouble(parts[0]) + Double.parseDouble(parts[1]);
                            str_replacement = result.toString();
                        }
                    }
                    if (substr.indexOf(str_minus) > -1) {
                        String[] parts = substr.split("\\" + str_minus);
                        if (parts.length > 1) {
                            Double result = Double.parseDouble(parts[0]) - Double.parseDouble(parts[1]);
                            str_replacement = result.toString();
                        }
                    }
                }
                str_to_parse = str_to_parse.replaceFirst(str_regex, str_replacement);
                matcher = pattern.matcher(str_to_parse);
            }
        }

        if (str_to_parse.indexOf(str_leftparenthesis) > -1 || str_to_parse.indexOf(str_rightparenthesis) > -1) {
            show_error_message("parentheses!");
            return "";
        }

        return str_to_parse;
    }

    private void show_error_message(String str) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button0:
                addToMainText(getString(R.string.str_zero));
                break;
            case R.id.button1:
                addToMainText(getString(R.string.str_one));
                break;
            case R.id.button2:
                addToMainText(getString(R.string.str_two));
                break;
            case R.id.button3:
                addToMainText(getString(R.string.str_three));
                break;
            case R.id.button4:
                addToMainText(getString(R.string.str_four));
                break;
            case R.id.button5:
                addToMainText(getString(R.string.str_five));
                break;
            case R.id.button6:
                addToMainText(getString(R.string.str_six));
                break;
            case R.id.button7:
                addToMainText(getString(R.string.str_seven));
                break;
            case R.id.button8:
                addToMainText(getString(R.string.str_eight));
                break;
            case R.id.button9:
                addToMainText(getString(R.string.str_nine));
                break;
            case R.id.button_leftparenthesis:
                addToMainText(getString(R.string.str_leftparenthesis));
                break;
            case R.id.button_rightparenthesis:
                addToMainText(getString(R.string.str_rightparenthesis));
                break;
            case R.id.button_point:
                addToMainText(getString(R.string.str_point));
                break;
            case R.id.button_minus:
                addToMainText(getString(R.string.str_minus));
                break;
            case R.id.button_plus:
                addToMainText(getString(R.string.str_plus));
                break;
            case R.id.button_division:
                addToMainText(getString(R.string.str_division));
                break;
            case R.id.button_multiply:
                addToMainText(getString(R.string.str_multiply));
                break;
            case R.id.button_clear:
                clear();
                break;
            case R.id.button_backspace:
                backspace();
                break;
            case R.id.button_equal:
                equal();
                break;
        }
        displayText(main_text);
    }

}