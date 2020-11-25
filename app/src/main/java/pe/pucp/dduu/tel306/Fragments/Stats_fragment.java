package pe.pucp.dduu.tel306.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.chart.common.listener.Event;
import com.anychart.chart.common.listener.ListenersInterface;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;


import java.util.ArrayList;
import java.util.List;

import pe.pucp.dduu.tel306.Classes.AnswerClass;
import pe.pucp.dduu.tel306.Classes.AnswerStatsClass;
import pe.pucp.dduu.tel306.Classes.DtoAnswers;
import pe.pucp.dduu.tel306.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Stats_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Stats_fragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static DtoAnswers dto;
    private static Context contexto;

    public Stats_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Stats_fragment.
     */
    public static Stats_fragment newInstance(DtoAnswers dt,Context c) {
        Stats_fragment fragment = new Stats_fragment();
        dto=dt;
        contexto = c;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stats_fragment, container, false);

        AnyChartView anyChartView = view.findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(view.findViewById(R.id.progress_bar));

        Pie pie = AnyChart.pie();

        pie.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value"}) {
            @Override
            public void onClick(Event event) {
                Toast.makeText(contexto, event.getData().get("x") + ": " + event.getData().get("value"), Toast.LENGTH_SHORT).show();
            }
        });

        List<DataEntry> data = new ArrayList<>();

        for (AnswerStatsClass as : dto.getAnswerstats()){
            data.add(new ValueDataEntry(as.getAnswer().getAnswerText(), Integer.parseInt(as.getCount())));

        }

        pie.data(data);

        pie.title(dto.getQuestion().getQuestionText());

        pie.labels().position("outside");

        pie.legend().title().enabled(true);
        pie.legend().title()
                .text("Respuestas")
                .padding(0d, 0d, 10d, 0d);

        pie.legend()
                .position("center-bottom")
                .itemsLayout(LegendLayout.HORIZONTAL)
                .align(Align.CENTER);

        anyChartView.setChart(pie);

        Log.d("debugeo", "FIN DE CHART");
        return view;
    }

//    private int getTotalCount(){
//        int count= 0;
//        for (AnswerStatsClass as : answerStats){
//            count= count + Integer.parseInt(as.getCount());
//        }
//        return count;
//    }

}