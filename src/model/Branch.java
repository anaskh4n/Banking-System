package model;

public class Branch {
    private int branchId;
    private String branchName, city, managerName, contact;

    public Branch() {}
    public Branch(String branchName, String city, String managerName, String contact) {
        this.branchName = branchName; this.city = city;
        this.managerName = managerName; this.contact = contact;
    }

    public int getBranchId() { return branchId; }
    public void setBranchId(int v) { branchId = v; }
    public String getBranchName() { return branchName; }
    public void setBranchName(String v) { branchName = v; }
    public String getCity() { return city; }
    public void setCity(String v) { city = v; }
    public String getManagerName() { return managerName; }
    public void setManagerName(String v) { managerName = v; }
    public String getContact() { return contact; }
    public void setContact(String v) { contact = v; }
    
    @Override public String toString() { return branchId + " - " + branchName; }
}
