package com.vertex.vertex.property.model.ENUM;

import com.vertex.vertex.property.model.entity.PropertyList;
import com.vertex.vertex.task.value.model.entity.*;

import java.io.File;
import java.sql.Date;

public enum PropertyKind  {

    TEXT(){
        public Value getValue() {
            return new ValueText();
        }
    },
    DATE() {
        public Value getValue() {

            return new ValueDate();
        }
    },
    LIST() {
        public Value getValue() {

            return new ValueList();
        }
    },
    STATUS() {
        public Value getValue() {

            return new ValueList();
        }
    },
    NUMBER() {
        public Value getValue() {
            return new ValueNumber();
        }
    },
    LINK() {
        public Value getValue() {
            return new ValueFile();
        }
    };

    public abstract Value getValue();
}
