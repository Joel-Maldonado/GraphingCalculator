        for (int x = min * units; x <= max * units; x += units) {
            g.fillOval(x + xOffset - plotPointSize/2, -f(x) + yOffset - plotPointSize/2, plotPointSize, plotPointSize);
            System.out.println(String.valueOf((x + xOffset - plotPointSize/2)/units) + " " + String.valueOf((-f(x) * units + yOffset - plotPointSize/2)/units));
        }