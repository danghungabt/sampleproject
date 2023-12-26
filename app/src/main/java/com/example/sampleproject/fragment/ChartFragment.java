package com.example.sampleproject.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.sampleproject.LocalStorage;
import com.example.sampleproject.R;
import com.example.sampleproject.adapter.CustomSpinnerAdapter;
import com.example.sampleproject.api.ExportDataApi;
import com.example.sampleproject.api.TokenManager;
import com.example.sampleproject.model.WeatherAssetModel;
import com.example.sampleproject.repository.WeatherAssetRepository;
import com.example.sampleproject.viewmodel.DashboardBasicViewModel;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TimeZone;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class ChartFragment extends Fragment {
    private final TokenManager tokenManager = TokenManager.getInstance(getContext());
    View view;
    public static long last_time;
    public static int axis_x_format;
    public static int mode;
    Paint paint;
    Button show_btn;
    Map<Date, Float> data;
    GraphView graph;
    LineGraphSeries<DataPoint> series;
    Handler ui_handler = new Handler();
    Spinner attribute_spinner, timeframe_spinner, assetSpinner;

    private String timeFrame = "";
    private RelativeLayout rlContent;
    private TextView tvInputDateTime;
    private Calendar calendarEnding;
    private ProgressBar prbLoadingChart, loadingProgressBar;
    private List<WeatherAssetModel> weatherAssetModelList;
    private WeatherAssetModel weatherAssetModel;
    private CustomSpinnerAdapter customSpinnerAdapter;
    private ArrayList<String> attribute_ArrayList;
    private ArrayAdapter<String> attribute_adapter;
    private String assetId, attrName;
    private RelativeLayout background;


    String getQueryAttribute() {
        String template = "[{\"id\":\"%s\",\"name\":\"%s\"}]";

        if (assetId != null && !assetId.equals("") && attrName != null && !attrName.equals("")) {
            return String.format(template, assetId, attrName);
        }
        return null;
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_chart, container, false);
        show_btn = view.findViewById(R.id.btn_show);
        timeframe_spinner = view.findViewById(R.id.spinner_timeframe);
        graph = view.findViewById(R.id.idGraphView);
        tvInputDateTime = view.findViewById(R.id.tvInputDateTime);
        attribute_spinner = view.findViewById(R.id.spinner_attribute);
        prbLoadingChart = view.findViewById(R.id.prbLoadingChart);
        assetSpinner = view.findViewById(R.id.spinner_asset);
        rlContent = view.findViewById(R.id.rlContent);
        loadingProgressBar = view.findViewById(R.id.loadingProgressBar);
        background = view.findViewById(R.id.background4);

        loadData();

        calendarEnding = Calendar.getInstance(TimeZone.getTimeZone("GMT+7"));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String dateTime = simpleDateFormat.format(calendarEnding.getTime());
        tvInputDateTime.setText(dateTime);


        tvInputDateTime.setOnClickListener(v -> showDateTimePicker());

        assetSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                weatherAssetModel = weatherAssetModelList.get(position);
                assetId = weatherAssetModel.id;
                WeatherAssetModel.Attributes attributes = weatherAssetModel.attributes;
                List<String> attributeNames = new ArrayList<>();

                List<WeatherAssetModel.Attributes.Measurement> measurementList = getWeatherList(attributes);
                List<WeatherAssetModel.Attributes.Pollutant> pollutantList = getPollutantList(attributes);
                attributeNames.addAll(measurementList.stream().filter(item1 -> item1.value != null).map(item -> item.name).collect(Collectors.toList()));
                attributeNames.addAll(pollutantList.stream().filter(item1 -> item1.value != null).map(item -> item.name).collect(Collectors.toList()));

                attrName = attributeNames.get(0);
                attribute_ArrayList.clear();
                attribute_ArrayList.addAll(attributeNames);
                attribute_adapter.notifyDataSetChanged();
                attribute_spinner.setSelection(0);
//                background.setBackgroundResource(R.color.white);
                if(attrName.equals("humidity")){
                    background.setBackgroundResource(R.drawable.bg_chart_humidity);
                }else if(attrName.equals("AQI")){
                    background.setBackgroundResource(R.drawable.bg_chart_aqi);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        timeframe_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                switch (item) {
                    case "Day":
                    case "Ngày":
                        ChartFragment.last_time = Long.parseLong("1") * 86400 * 1000;
                        ChartFragment.axis_x_format = 0;
                        timeFrame = "day";
                        break;
                    case "Week":
                    case "Tuần":
                        ChartFragment.last_time = Long.parseLong("1") * 86400 * 7 * 1000;
                        ChartFragment.axis_x_format = 1;
                        timeFrame = "week";
                        break;
                    case "Month":
                    case "Tháng":
                        ChartFragment.last_time = Long.parseLong("1") * 86400 * 30 * 1000;
                        ChartFragment.axis_x_format = 2;
                        timeFrame = "month";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        ArrayList<String> timeframes = new ArrayList<>();

        SharedPreferences sharedPreferences = LocalStorage.getInstance(getContext());
        String language = sharedPreferences.getString("language", "en");
        if(language.equals("en")){
            timeframes.add("Day");
            timeframes.add("Week");
            timeframes.add("Month");
        }else {
            timeframes.add("Ngày");
            timeframes.add("Tuần");
            timeframes.add("Tháng");
        }


        ArrayAdapter adapter = new ArrayAdapter(
                view.getContext(),
                R.layout.spinner_item,
                timeframes
        );
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        timeframe_spinner.setAdapter(adapter);
        timeframe_spinner.setSelection(0);


        paint = new Paint();
        int textColor = ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.primaryColor);
        paint.setColor(textColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        paint.setPathEffect(new DashPathEffect(new float[]{8, 5}, 0));

        attribute_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                attrName = parent.getItemAtPosition(position).toString();
                switch (attrName){
                    case "rainfall":
                        background.setBackgroundResource(R.drawable.bg_chart_rainfall);
                        break;
                    case "humidity":
                        background.setBackgroundResource(R.drawable.bg_chart_humidity);
                        break;
                    case "temperature":
                        background.setBackgroundResource(R.drawable.bg_chart_temperature);
                        break;
                    case "windDirection":
                    case "windSpeed":
                        background.setBackgroundResource(R.drawable.bg_chart_wind);
                        break;
                    default:
                        background.setBackgroundResource(R.drawable.bg_chart_aqi);
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        show_btn.setBackgroundResource(R.drawable.custom_button_rounded);
        attribute_spinner.setBackgroundResource(R.drawable.custom_spinner);
        timeframe_spinner.setBackgroundResource(R.drawable.custom_spinner);

        DefaultLabelFormatter custom_formatter = new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+7"));
                    calendar.setTimeInMillis((long) value);
                    if (ChartFragment.axis_x_format == 0)
                        return String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)).concat(":00");
                    else if (ChartFragment.axis_x_format == 3)
                        return String.valueOf(calendar.get(Calendar.MONTH) + 1).concat("/" + (calendar.get(Calendar.YEAR)));
                    else
                        return String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)).concat("/" + (calendar.get(Calendar.MONTH) + 1));

                }
                DecimalFormat df = new DecimalFormat("0.0");

                switch (attrName) {
                    case "humidity":
                    case "windDirection":
                        df = new DecimalFormat("0");
                        return df.format(value);
                    case "rainfall":
                    case "temperature":
                    case "windSpeed":
                    case "SO2":
                    case "NO2":
                        df = new DecimalFormat("0.00");
                        return df.format(value);
                    default:
                        return df.format(value);
                }
            }
        };


        show_btn.setOnClickListener(v -> {
            Log.d("Show", String.valueOf(ChartFragment.last_time));
            graph.setVisibility(View.VISIBLE);
            prbLoadingChart.setVisibility(View.VISIBLE);
            graph.setTitleColor(R.color.green_primary);
            graph.setCursorMode(true);
            Log.d("Show", String.valueOf(ChartFragment.last_time));
            if (ChartFragment.mode == 0) {
                Map<String, String> query = new HashMap<>();
                query.put("attributeRefs", getQueryAttribute());

                long to_timestamp = calendarEnding.getTimeInMillis();
                long from_timestamp = to_timestamp - ChartFragment.last_time;
                query.put("fromTimestamp", String.valueOf(from_timestamp));
                query.put("toTimestamp", String.valueOf(to_timestamp));
                Thread data_thread = new Thread(() -> {
                    try {
                        Log.d("interrupt", "false");
                        ExportDataApi export_data = new ExportDataApi("https://uiot.ixxc.dev/api/master/asset/datapoint/export", "GET", query, tokenManager.getAccessToken());
//                                ExportDataApi export_data = new ExportDataApi("https://uiot.ixxc.dev/api/master/asset/datapoint/export", "GET", query, ClientAPI.getToken());
                        data = export_data.GetData(axis_x_format);
                        SortedSet<Date> keys = new TreeSet<>(data.keySet());
                        DataPoint[] datapoints_temp = new DataPoint[keys.size()];

                        if (data.isEmpty()) {
                            ui_handler.post(() -> {
                                graph.removeAllSeries();
                                Toast.makeText(getContext(), "Empty data", Toast.LENGTH_SHORT).show();
                                series = new LineGraphSeries<>();
                                series.setCustomPaint(paint);
                                graph.setTitleTextSize(40);
                                graph.setTitleColor(textColor);
                                series.setDrawDataPoints(true);
                                series.setDataPointsRadius(10);
                                graph.getViewport().setYAxisBoundsManual(true);
                                switch (attrName) {
                                    case "temperature":
                                        graph.setTitle(getResources().getString(R.string.popup_temperature_c));
                                        graph.getViewport().setMinY(0);
                                        graph.getViewport().setMaxY(35);
                                        break;
                                    case "humidity":
                                        graph.setTitle(getResources().getString(R.string.popup_humidity));
                                        graph.getViewport().setMinY(0);
                                        graph.getViewport().setMaxY(100);
                                        break;
                                    case "rainfall":
                                        graph.setTitle(getResources().getString(R.string.popup_rainfall_mm));
                                        graph.getViewport().setMinY(0);
                                        graph.getViewport().setMaxY(5);
                                        break;
                                    case "windSpeed":
                                        graph.setTitle(getResources().getString(R.string.popup_wind_speed_m_s));
                                        graph.getViewport().setMinY(0);
                                        if (timeFrame.equals("day")) {
                                            graph.getViewport().setMaxY(6);
                                        } else {
                                            graph.getViewport().setMaxY(7);
                                        }
                                        break;
                                    case "windDirection":
                                        graph.setTitle(getResources().getString(R.string.popup_wind_direction));
                                        graph.getViewport().setMinY(0);
                                        graph.getViewport().setMaxY(100);
                                        break;
                                    case "AQI_Predict":
                                        graph.setTitle("AQ i_predict");
                                        graph.getViewport().setMinY(0);
                                        if (timeFrame.equals("day")) {
                                            graph.getViewport().setMaxY(1);
                                        } else {
                                            graph.getViewport().setMaxY(2);
                                        }
                                        break;
                                    case "AQI":
                                        graph.setTitle("AQI");
                                        graph.getViewport().setMinY(0);
                                        graph.getViewport().setMaxY(1);
                                        break;
                                    case "CO2":
                                        graph.setTitle("CO2");
                                        graph.getViewport().setMinY(0);
                                        graph.getViewport().setMaxY(500);
                                        break;
                                    case "CO2_average":
                                        graph.setTitle("CO2_average");
                                        graph.getViewport().setMinY(0);
                                        graph.getViewport().setMaxY(500);
                                        break;
                                    case "PM10":
                                        graph.setTitle("PM10");
                                        graph.getViewport().setMinY(0);
                                        graph.getViewport().setMaxY(8);
                                        break;
                                    case "PM25":
                                        graph.setTitle("PM25");
                                        graph.getViewport().setMinY(0);
                                        if (timeFrame.equals("day")) {
                                            graph.getViewport().setMaxY(5);
                                        } else {
                                            graph.getViewport().setMaxY(6);
                                        }
                                        break;
                                    case "NO2":
                                        graph.setTitle("NO2");
                                        graph.getViewport().setMinY(0);
                                        if (timeFrame.equals("day")) {
                                            graph.getViewport().setMaxY(30);
                                        } else {
                                            graph.getViewport().setMaxY(40);
                                        }
                                        break;
                                    case "SO2":
                                        graph.setTitle("SO2");
                                        graph.getViewport().setMinY(0);
                                        if (timeFrame.equals("day")) {
                                            graph.getViewport().setMaxY(40);
                                        } else {
                                            graph.getViewport().setMaxY(45);
                                        }
                                        break;

                                }
                                graph.getViewport().setXAxisBoundsManual(true);
                                graph.addSeries(series);
                                graph.getGridLabelRenderer().setLabelFormatter(custom_formatter);

                                graph.setCursorMode(false);
                                graph.getViewport().setScrollable(false);
                                switch (timeFrame) {
                                    case "day":
                                        graph.getViewport().setMaxX(calendarEnding.getTimeInMillis());
                                        graph.getViewport().setMinX(calendarEnding.getTimeInMillis() - 86400 * 1000);
                                        break;
                                    case "week":
                                        graph.getViewport().setMaxX(calendarEnding.getTimeInMillis());
                                        graph.getViewport().setMinX(calendarEnding.getTimeInMillis() - 86400 * 7 * 1000);
                                        break;
                                    case "month":
                                        graph.setCursorMode(false);
                                        graph.getViewport().setScrollable(true);
                                        graph.getViewport().setMaxX(calendarEnding.getTimeInMillis());
                                        graph.getViewport().setMinX(calendarEnding.getTimeInMillis() - 86400L * 30 * 1000);
                                        break;
                                }
                            });
                        } else {
                            int count = 0;
                            for (Date key : keys) {
                                datapoints_temp[count] = new DataPoint(key, data.get(key));
                                count++;
                            }
                            series = new LineGraphSeries<>(datapoints_temp);

                            ui_handler.post(() -> {
                                graph.removeAllSeries();
                                graph.setTitleTextSize(40);
                                graph.setTitleColor(textColor);
                                series.setCustomPaint(paint);
                                series.setDrawDataPoints(true);
                                series.setDataPointsRadius(10);
                                graph.getViewport().setYAxisBoundsManual(true);
                                switch (attrName) {
                                    case "temperature":
                                        graph.setTitle(getResources().getString(R.string.popup_temperature_c));
                                        graph.getViewport().setMinY(0);
                                        graph.getViewport().setMaxY(35);
                                        break;
                                    case "humidity":
                                        graph.setTitle(getResources().getString(R.string.popup_humidity));
                                        graph.getViewport().setMinY(0);
                                        graph.getViewport().setMaxY(100);
                                        break;
                                    case "rainfall":
                                        graph.setTitle(getResources().getString(R.string.popup_rainfall_mm));
                                        graph.getViewport().setMinY(0);
                                        graph.getViewport().setMaxY(5);
                                        break;
                                    case "windSpeed":
                                        graph.setTitle(getResources().getString(R.string.popup_wind_speed_m_s));
                                        graph.getViewport().setMinY(0);
                                        if (timeFrame.equals("day")) {
                                            graph.getViewport().setMaxY(6);
                                        } else {
                                            graph.getViewport().setMaxY(7);
                                        }
                                        break;
                                    case "windDirection":
                                        graph.setTitle(getResources().getString(R.string.popup_wind_direction));
                                        graph.getViewport().setMinY(0);
                                        graph.getViewport().setMaxY(100);
                                        break;
                                    case "AQI_Predict":
                                        graph.setTitle("AQ i_predict");
                                        graph.getViewport().setMinY(0);
                                        if (timeFrame.equals("day")) {
                                            graph.getViewport().setMaxY(1);
                                        } else {
                                            graph.getViewport().setMaxY(2);
                                        }
                                        break;
                                    case "AQI":
                                        graph.setTitle("AQI");
                                        graph.getViewport().setMinY(0);
                                        graph.getViewport().setMaxY(1);
                                        break;
                                    case "CO2":
                                        graph.setTitle("CO2");
                                        graph.getViewport().setMinY(0);
                                        graph.getViewport().setMaxY(500);
                                        break;
                                    case "CO2_average":
                                        graph.setTitle("CO2_average");
                                        graph.getViewport().setMinY(0);
                                        graph.getViewport().setMaxY(500);
                                        break;
                                    case "PM10":
                                        graph.setTitle("PM10");
                                        graph.getViewport().setMinY(0);
                                        graph.getViewport().setMaxY(8);
                                        break;
                                    case "PM25":
                                        graph.setTitle("PM25");
                                        graph.getViewport().setMinY(0);
                                        if (timeFrame.equals("day")) {
                                            graph.getViewport().setMaxY(5);
                                        } else {
                                            graph.getViewport().setMaxY(6);
                                        }
                                        break;
                                    case "NO2":
                                        graph.setTitle("NO2");
                                        graph.getViewport().setMinY(0);
                                        if (timeFrame.equals("day")) {
                                            graph.getViewport().setMaxY(30);
                                        } else {
                                            graph.getViewport().setMaxY(40);
                                        }
                                        break;
                                    case "SO2":
                                        graph.setTitle("SO2");
                                        graph.getViewport().setMinY(0);
                                        if (timeFrame.equals("day")) {
                                            graph.getViewport().setMaxY(40);
                                        } else {
                                            graph.getViewport().setMaxY(45);
                                        }
                                        break;

                                }

                                graph.getViewport().setXAxisBoundsManual(true);
                                graph.addSeries(series);

                                graph.getGridLabelRenderer().setLabelFormatter(custom_formatter);

                                if (ChartFragment.axis_x_format == 0 || ChartFragment.axis_x_format == 1) {
                                    graph.getViewport().setMinX(datapoints_temp[0].getX());
                                    graph.setCursorMode(true);
                                    graph.getViewport().setScrollable(false);
                                    graph.getViewport().setMaxX(datapoints_temp[datapoints_temp.length - 1].getX());
                                } else {
                                    graph.getViewport().setMinX(datapoints_temp[0].getX());
                                    graph.getViewport().setMaxX(datapoints_temp[(datapoints_temp.length - 1)].getX());
                                    graph.setCursorMode(true);
                                    graph.getViewport().setScrollable(true);
                                }
                            });
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                data_thread.start();
                try {
                    // Đợi cho đến khi data_thread kết thúc
                    data_thread.join();
                    // Đoạn code sau khi data_thread kết thúc
                    prbLoadingChart.setVisibility(View.INVISIBLE);
                } catch (InterruptedException e) {
                    // Xử lý nếu có lỗi khi chờ thread kết thúc
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    private void loadData() {

        attribute_ArrayList = new ArrayList<>();
        attribute_adapter = new ArrayAdapter<>(view.getContext(),
                R.layout.spinner_item, // Sử dụng tệp layout tùy chỉnh ở đây
                attribute_ArrayList
        );

        attribute_adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        attribute_spinner.setAdapter(attribute_adapter);
        WeatherAssetRepository weatherAssetRepository = new WeatherAssetRepository(getContext());
        DashboardBasicViewModel dashboardViewModel = new DashboardBasicViewModel(weatherAssetRepository);
        dashboardViewModel.getData().observe(getViewLifecycleOwner(), weatherAssets -> {
            if (weatherAssets != null) {
                customSpinnerAdapter = new CustomSpinnerAdapter(Objects.requireNonNull(getContext()), R.layout.item_spinner_custom, weatherAssets);
                assetSpinner.setAdapter(customSpinnerAdapter);
                weatherAssetModelList = weatherAssets;

                loadingProgressBar.setVisibility(View.GONE);
                rlContent.setVisibility(View.VISIBLE);
            }
        });
    }

    private void showDateTimePicker() {
        int year = calendarEnding.get(Calendar.YEAR);
        int month = calendarEnding.get(Calendar.MONTH);
        int day = calendarEnding.get(Calendar.DAY_OF_MONTH);

        TimePickerDialog.OnTimeSetListener timeSetListener = (view, hourOfDay, minute) -> {
            calendarEnding.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendarEnding.set(Calendar.MINUTE, minute);

            // Định dạng ngày và giờ và hiển thị trong textView
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            String dateTime = simpleDateFormat.format(calendarEnding.getTime());
            tvInputDateTime.setText(dateTime);
        };

        DatePickerDialog.OnDateSetListener dateSetListener = (view, year1, month1, dayOfMonth) -> {
            calendarEnding.set(Calendar.YEAR, year1);
            calendarEnding.set(Calendar.MONTH, month1);
            calendarEnding.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            int hour = calendarEnding.get(Calendar.HOUR_OF_DAY);
            int minute = calendarEnding.get(Calendar.MINUTE);

            // Tạo TimePickerDialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), timeSetListener, hour, minute, DateFormat.is24HourFormat(getContext()));
            timePickerDialog.show();
        };

        // Tạo DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), dateSetListener, year, month, day);
        datePickerDialog.show();
    }

    private List<WeatherAssetModel.Attributes.Measurement> getWeatherList(WeatherAssetModel.Attributes attributes) {
        // Loop through each field using reflection
        List<WeatherAssetModel.Attributes.Measurement> values = new ArrayList<>();
        for (Field field : WeatherAssetModel.Attributes.class.getDeclaredFields()) {
            try {
                Object value = field.get(attributes);
                if (value instanceof WeatherAssetModel.Attributes.Measurement) {
                    values.add((WeatherAssetModel.Attributes.Measurement) value);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return values;
    }

    private List<WeatherAssetModel.Attributes.Pollutant> getPollutantList(WeatherAssetModel.Attributes attributes) {
        // Loop through each field using reflection
        List<WeatherAssetModel.Attributes.Pollutant> values = new ArrayList<>();
        for (Field field : WeatherAssetModel.Attributes.class.getDeclaredFields()) {
            try {
                Object value = field.get(attributes);
                if (value instanceof WeatherAssetModel.Attributes.Pollutant) {
                    values.add((WeatherAssetModel.Attributes.Pollutant) value);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return values;
    }
}