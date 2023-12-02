#pragma once

#include <Ice/BuiltinSequences.ice>

module Sorting
{   
    interface Sorter
    {
        ["java:type:java.util.ArrayList<String>:java.util.List<String>"] Ice::StringSeq sortArray(["java:type:java.util.ArrayList<String>:java.util.List<String>"] Ice::StringSeq inputSequence);
    }

    interface WorkersHandler
    {
        void registerWorker(Sorter* proxy);
        void unregisterWorker(Sorter* proxy);
    }

}