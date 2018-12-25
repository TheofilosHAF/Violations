package violations.utils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Pattern;

public class ViolationEvent {

        private int no;
        private String date;
        private int formation;
        private int jets;
        private int armed;
        private int cn235;
        private int helis;
        private int totalPlanes;
        private int atrViol;
        private int naViol;
        private int dogfights;
        private int overflight;
        private String location;


        private ViolationEvent(int no, String date, int formation, int jets, int armed, int cn235, int helis, int totalPlanes, int atrViol, int naViol, int dogfights, int overflight, String location) {
            this.no = no;
            this.date = date;
            this.formation = formation;
            this.jets = jets;
            this.armed = armed;
            this.cn235 = cn235;

            this.helis = helis;
            this.totalPlanes = totalPlanes;
            this.atrViol = atrViol;
            this.naViol = naViol;

            this.dogfights = dogfights;
            this.overflight = overflight;
            this.location = location;
        }

        public String getLocation() {
            return location;
        }

        public int getDogfights() {
            return dogfights;
        }

        public String getDate() {
            return date;
        }

        public int getNo() {
            return no;
        }

        public int getFormation() {
            return formation;
        }

        public int getJets() {
            return jets;
        }

        public int getArmed() {
            return armed;
        }

        public int getCn235() {
            return cn235;
        }

        public int getHelis() {
            return helis;
        }

        public int getTotalPlanes() {
            return totalPlanes;
        }

        public int getAtrViol() {
            return atrViol;
        }

        public int getNaViol() {
            return naViol;
        }

        public int getOverflight() {
            return overflight;
        }

        @Override
        public String toString() {
            return
                    no +"η παραβίαση αυτό το μήνα\n\n" +
                    "Ημ/νια: "  + date + "\n" +
                    "Σχηματισμοί: " + formation + "\n" +
                    "Μαχητικά: " + jets + "\n" +
                    "Οπλισμένα: " + armed + "\n" +
                    "Α/φη CN-235: " + cn235 + "\n" +
                    "Ελικόπτερα: " + helis + "\n\n" +
                    "Σύνολο α/φων: " + totalPlanes + "\n\n" +
                    "Παραβιάσεις Κ.Ε.Κ: " + atrViol + "\n" +
                    "Παραβιάσεις Ε.Ε.Χ: " + naViol + "\n" +
                    "Εμπλοκές: " + dogfights + "\n" +
                    "Υπερπτήσεις: " + overflight + "\n\n" +
                    "Περιοχή: " + location;
        }

    @NotNull
    @Contract("_ -> new")
    public static ViolationEvent getViolationEvent(@NotNull Elements row) {
        Pattern pattern = Pattern.compile("&nbsp;|-| |");
        int no = 0;
        if (!pattern.matcher(row.get(1).text()).matches())
            no = Integer.parseInt(row.get(0).text());

        String date = row.get(1).text();

        int formation = 0;
        if (!pattern.matcher(row.get(2).text()).matches())
            formation = Integer.parseInt(row.get(2).text());

        int jets = 0;
        if (!pattern.matcher(row.get(3).text()).matches())
            jets = Integer.parseInt(row.get(3).text());

        int armed = 0;
        if (!pattern.matcher(row.get(4).text()).matches())
            armed = Integer.parseInt(row.get(4).text());

        int cn235 = 0;
        if (!pattern.matcher(row.get(6).text()).matches())
            cn235 = Integer.parseInt(row.get(6).text());

        int helis = 0;
        if (!pattern.matcher(row.get(7).text()).matches())
            helis = Integer.parseInt(row.get(7).text());

        int totalPlanes = 0;
        if (!pattern.matcher(row.get(8).text()).matches())
            totalPlanes = Integer.parseInt(row.get(8).text());

        int atrViol = 0;
        if (!pattern.matcher(row.get(9).text()).matches())
            atrViol = Integer.parseInt(row.get(9).text());

        int naViol = 0;
        if (!pattern.matcher(row.get(10).text()).matches())
            naViol = Integer.parseInt(row.get(10).text());

        int dogfights = 0;
        if (!pattern.matcher(row.get(11).text()).matches())
            dogfights = Integer.parseInt(row.get(11).text());

        int overflight = 0;
        if (!pattern.matcher(row.get(12).text()).matches())
            overflight = Integer.parseInt(row.get(12).text());


        String location = row.get(13).text();

        return new ViolationEvent(no, date, formation, jets, armed, cn235, helis, totalPlanes, atrViol, naViol, dogfights, overflight, location);
    }

    @Nullable
    public static ArrayList<ViolationEvent> getViolationsList() {
        Document site;
        try {
            site = Jsoup.connect("http://www.geetha.mil.gr/el/violations-gr/2015-01-22-11-43-23.html").timeout(6000).get();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        Element table = Objects.requireNonNull(site).select("table").get(1);
        Elements rows = table.select("tr");
        ArrayList<ViolationEvent> violations = new ArrayList<>();
        for (int i = 2; i < rows.size(); i++) { //first 2 rows are the table title names.
            Elements row = rows.get(i).select("td");

            ViolationEvent violation = ViolationEvent.getViolationEvent(row);
            violations.add(violation);

        }
        return violations;
    }

    @Nullable
    public static Violations getViolations() {
        ArrayList<ViolationEvent> violationEvents = getViolationsList();
        if (violationEvents == null) return null;
        return new Violations(violationEvents);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ViolationEvent)) return false;
        ViolationEvent that = (ViolationEvent) o;
        return no == that.no &&
                formation == that.formation &&
                jets == that.jets &&
                armed == that.armed &&
                cn235 == that.cn235 &&
                helis == that.helis &&
                totalPlanes == that.totalPlanes &&
                atrViol == that.atrViol &&
                naViol == that.naViol &&
                dogfights == that.dogfights &&
                overflight == that.overflight &&
                date.equals(that.date) &&
                location.equals(that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(no, date, formation, jets, armed, cn235, helis, totalPlanes, atrViol, naViol, dogfights, overflight, location);
    }
}
