var counters = {};

var context = cubism.context()
    .step(5000)
    .size(820);

var horizon = context.horizon();

d3.select("#counters").selectAll(".axis")
    .data(["top"])
    .enter().append("div")
    .attr("class", function (d) {
        return d + " axis";
    })
    .each(function (d) {
        d3.select(this).call(context.axis().ticks(12).orient(d));
    });

// d3.select("#counters").append("div")
//     .attr("class", "rule")
//     .call(context.rule());

context.on("focus", function (i) {
    d3.selectAll(".value").style("right", i == null ? null : context.size() - i + "px");
});

function newCounter(name, counterEvent) {
    var label = name.replace(new RegExp("\\.", 'g'), "-");
    var key = label + "-counter-container";

    counters[name] = {
        config: {
            label: label,
            key: key,
            name: name,
            eventDelay: 560,
            rendered: false,
            min: 0,
            max: 0
        },
        events: []
    };

    applyCounterEvent(counters[name], counterEvent);

    return counters[name];
}

function applyCounterEvent(counter, counterEvent) {
    counter.events.push(counterEvent);

    var val = counterEvent.value;
    counter.config.min = counter.config.min > val ? val : counter.config.min;
    counter.config.max = counter.config.max < val ? val : counter.config.max;

    if (counter.config.eventDelay > 0) {
        counter.config.eventDelay--;
    } else if (!counter.config.rendered) {
        counter.config.rendered = true;
        renderHorizon();
    }
}

function createMetric(counter) {
     var metric = context.metric(function (start, stop, step, callback) {
        var values = [];
        start = +start;
        stop = +stop;

        while (start < stop) {
            var val = counters[counter.config.name].events.filter(function (item) {
                return item.timestamp < (start + step) && item.timestamp > start;
            }).reduce(function (a, b) {
                return a + b.value;
            }, 0);

            counter.config.min = counter.config.min > val ? val : counter.config.min;
            counter.config.max = counter.config.max < val ? val : counter.config.max;

            values.push(val);

            start += step;
        }

        metric.extent([counter.config.min, counter.config.max]);

        callback(null, values);
    }, counter.config.name);

    counter.config.metric = metric;

    return metric;
}

var horizons;

function renderHorizon() {
    horizons = d3.select("#counters").selectAll(".horizon")
        .data(Object.values(counters).map(createMetric))
        .enter()
        .insert("div", ".bottom")
        .attr("class", function (d, i) {
            return "horizon " + Object.values(counters)[i].config.label;
        });

    horizons.call(function (d) {
        horizon.height(50).scale(d3.scale.linear()
            .domain([0, 400])
            .interpolate(d3.interpolateNumber)
            .range([-30, 30]));

        horizon(d);
    });
}