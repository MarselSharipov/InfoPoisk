package com.github.marselsharipov;

import com.github.marselsharipov.grabber.Grabber;
import com.github.marselsharipov.grabber.output.GrabberOutput;
import com.github.marselsharipov.processor.PageRankProcessor;
import com.github.marselsharipov.processor.ProcessorData;
import com.github.marselsharipov.tools.Options;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.Comparator;

public class App {

    public static void main(String[] args) {

        String site = "https://kpfu.ru/";

        boolean sparsedMatrix = false;

        int pagesNumber = 1;

        int crawlerThreads = 1;

        int calculatorThreads = 0; // - 4я


        if (sparsedMatrix) {
            Options.put(Options.SPARSED_MATRIX, Boolean.TRUE);
        }

        if (pagesNumber != 0) {
            Options.put(Options.CRAWLER_THREADS, pagesNumber);
        }

        if (crawlerThreads != 0) {
            Options.put(Options.CRAWLER_THREADS, crawlerThreads);
        }

        if (calculatorThreads != 0) {
            Options.put(Options.CALCULATOR_THREADS, calculatorThreads);
        }

        final long start = System.currentTimeMillis();
        final GrabberOutput grabberOutput = Grabber.newInstance().work(site);
        final Collection<ProcessorData> result = PageRankProcessor.createInstance().process(grabberOutput);
        final long end = System.currentTimeMillis();

        System.out.println("Grabber Results:");
        grabberOutput.printAsMatrix(new PrintWriter(System.out));
        System.out.println("Calculated page ranks");
        result.stream().sorted(Comparator.comparing(ProcessorData::rank)).forEach(r -> System.out.println(r.page() + " : " + r.rank()));
        System.out.println("\n all time of process " + (end - start) + " ms");
    }
}
