package at.uibk.dps.dsB.task4;

import at.uibk.dps.dsB.task4.properties.PropertyProvider;
import at.uibk.dps.dsB.task4.properties.PropertyProviderDynamic;
import net.sf.opendse.model.*;

import java.util.HashMap;
import java.util.Map;

public class PropertyProviderDynamicAverage implements PropertyProvider {
    protected final PropertyProvider propertyProvider;

    private final int samples;

    private Integer avgCarNumber;
    private Integer avgNumberOfPeople;
    private Map<String,Integer> numberOfAvgAvalableInstances = new HashMap();
    private Map<String,Double> averageTransmissionTimes= new HashMap();
    private Map<String,Double> averageExecutionTimes= new HashMap();


    public PropertyProviderDynamicAverage(PropertyProvider propertyProvider, int samples){
        this.propertyProvider = propertyProvider;
        this.samples = samples;
    }

    public double getTransmissionTime(Communication communication, Link link){
        var key = communication.getId() + "_" + link.getId();
        var averageTransmissionTime = averageTransmissionTimes.get(key);
        if(averageTransmissionTime != null){
            return averageTransmissionTime;
        }
        double sum = 0;
        for(int i = 0; i < samples; i++){
            sum+= propertyProvider.getTransmissionTime(communication, link);
        }
        averageTransmissionTime = sum/samples;
        averageTransmissionTimes.put(key, averageTransmissionTime);
        return averageTransmissionTime;
    }

    public double getExecutionTime(Mapping<Task, Resource> mapping){
        var key = mapping.getId();
        var averageExecutionTime = averageExecutionTimes.get(key);
        if(averageExecutionTime != null){
            return averageExecutionTime;
        }
        double sum = 0;
        for(int i = 0; i < samples; i++){
            sum+= propertyProvider.getExecutionTime(mapping);
        }
        averageExecutionTime = sum/samples;
        averageExecutionTimes.put(key, averageExecutionTime);
        return averageExecutionTime;
    }

    public int getCarNumber(){
        if (avgCarNumber != null){
            return avgCarNumber;
        }

        int sum = 0;
        for(int i = 0; i < samples; i++){
            sum+= propertyProvider.getCarNumber();
        }
        avgCarNumber = sum/samples;
        return avgCarNumber;
    }

    public int getNumberOfPeople(){
        if (avgNumberOfPeople != null){
            return avgNumberOfPeople;
        }

        int sum = 0;
        for(int i = 0; i < samples; i++){
            sum+= propertyProvider.getNumberOfPeople();
        }
        avgNumberOfPeople = sum/samples;
        return avgNumberOfPeople;
    }

    public int getNumberOfAvailableInstances(Resource resource){
        var numberOfAvailableInstances = numberOfAvgAvalableInstances.get(resource.getId());
        if(numberOfAvailableInstances != null){
            return numberOfAvailableInstances;
        }
        int sum = 0;
        for(int i = 0; i < samples; i++){
            sum+= propertyProvider.getNumberOfAvailableInstances(resource);
        }
        numberOfAvailableInstances = sum/samples;
        numberOfAvgAvalableInstances.put(resource.getId(), numberOfAvailableInstances);
        return numberOfAvailableInstances;
    }
}
