package Models;

import java.util.ArrayList;

    public class Epic extends Task {

        private ArrayList<Integer> subtasksIDs = new ArrayList<>();

        public Epic(Integer id, String name, String description, String status) {
            super(id, name, description, status);
        }

    public ArrayList<Integer> getSubtasksIDs() {
        return subtasksIDs;
    }

    public void setSubtasksIDs(ArrayList<Integer> subtasksIDs) {
        this.subtasksIDs = subtasksIDs;
    }
}
