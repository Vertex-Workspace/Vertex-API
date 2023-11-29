package com.vertex.vertex.property.model.ENUM;

import com.vertex.vertex.task.value.model.entity.*;

import java.util.Optional;

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

//
//    TEXT() {
//        public Value getValue(Object object) {
//            return new ValueText((String) object);
//        }
//    },
//    DATE() {
//        public Value getValue(Object object) {
//            return new ValueDate((Date) object);
//        }
//    },
//    LIST() {
//        public Value getValue(Object object) {
//            return new ValueList((PropertyList) object);
//        }
//    },
//    STATUS() {
//        public Value getValue(Object object) {
//            return new ValueList((PropertyList) object);
//        }
//    },
//    NUMBER() {
//        public Value getValue(Object object) {
//            return new ValueNumber((Long) object);
//        }
//    },
//    LINK() {
//        public Value getValue(Object object) {
//            return new ValueFile((File) object);
//        }
//    };

    public abstract Value getValue();
}
