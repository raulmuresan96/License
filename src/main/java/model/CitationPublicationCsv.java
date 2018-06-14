package model;

/**
 * Created by Raul on 14/06/2018.
 */
public class CitationPublicationCsv {

    private byte[] publicationCsv;
    private byte[] citationCsv;

    public CitationPublicationCsv(byte[] publicationCsv, byte[] citationCsv) {
        this.publicationCsv = publicationCsv;
        this.citationCsv = citationCsv;
    }

    public CitationPublicationCsv(){

    }

    public byte[] getPublicationCsv() {
        return publicationCsv;
    }

    public void setPublicationCsv(byte[] publicationCsv) {
        this.publicationCsv = publicationCsv;
    }

    public byte[] getCitationCsv() {
        return citationCsv;
    }

    public void setCitationCsv(byte[] citationCsv) {
        this.citationCsv = citationCsv;
    }
}
