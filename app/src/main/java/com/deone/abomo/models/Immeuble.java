package com.deone.abomo.models;

public class Immeuble implements Comparable<Immeuble>{
    private String imid;
    private String imtitre;
    private String imdescription;
    private String imconstruction;
    private String imdate;

    public Immeuble() {
    }

    public Immeuble(String imid, String imtitre,
                    String imdescription, String imconstruction,
                    String imdate) {
        this.imid = imid;
        this.imtitre = imtitre;
        this.imdescription = imdescription;
        this.imconstruction = imconstruction;
        this.imdate = imdate;
    }

    public String getImid() {
        return imid;
    }

    public void setImid(String imid) {
        this.imid = imid;
    }

    public String getImtitre() {
        return imtitre;
    }

    public void setImtitre(String imtitre) {
        this.imtitre = imtitre;
    }

    public String getImdescription() {
        return imdescription;
    }

    public void setImdescription(String imdescription) {
        this.imdescription = imdescription;
    }

    public String getImconstruction() {
        return imconstruction;
    }

    public void setImconstruction(String imconstruction) {
        this.imconstruction = imconstruction;
    }

    public String getImdate() {
        return imdate;
    }

    public void setImdate(String imdate) {
        this.imdate = imdate;
    }

    @Override
    public int compareTo(Immeuble o) {
        return 0;
    }
}
