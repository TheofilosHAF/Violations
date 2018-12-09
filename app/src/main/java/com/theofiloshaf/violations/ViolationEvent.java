package com.theofiloshaf.violations;

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


        public ViolationEvent(int no, String date, int formation, int jets, int armed, int cn235, int helis, int totalPlanes, int atrViol, int naViol, int dogfights, int overflight, String location) {
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
                    "Περιοχή παραβίασης: " + location;
        }
}
