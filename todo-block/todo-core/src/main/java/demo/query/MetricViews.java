package demo.query;

import org.springframework.stereotype.Service;

@Service
public class MetricViews {

    private final TightCoupling tightCoupling;

    public MetricViews(TightCoupling tightCoupling) {
        this.tightCoupling = tightCoupling;
    }

    public TightCoupling getTightCoupling() {
        return tightCoupling;
    }
}
