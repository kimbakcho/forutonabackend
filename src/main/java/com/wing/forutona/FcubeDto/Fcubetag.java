package com.wing.forutona.FcubeDto;

public class Fcubetag {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column Fcubetag.cubeuuid
     *
     * @mbg.generated
     */
    private String cubeuuid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column Fcubetag.tagitem
     *
     * @mbg.generated
     */
    private String tagitem;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column Fcubetag.cubeuuid
     *
     * @return the value of Fcubetag.cubeuuid
     *
     * @mbg.generated
     */
    public String getCubeuuid() {
        return cubeuuid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column Fcubetag.cubeuuid
     *
     * @param cubeuuid the value for Fcubetag.cubeuuid
     *
     * @mbg.generated
     */
    public void setCubeuuid(String cubeuuid) {
        this.cubeuuid = cubeuuid == null ? null : cubeuuid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column Fcubetag.tagitem
     *
     * @return the value of Fcubetag.tagitem
     *
     * @mbg.generated
     */
    public String getTagitem() {
        return tagitem;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column Fcubetag.tagitem
     *
     * @param tagitem the value for Fcubetag.tagitem
     *
     * @mbg.generated
     */
    public void setTagitem(String tagitem) {
        this.tagitem = tagitem == null ? null : tagitem.trim();
    }
}