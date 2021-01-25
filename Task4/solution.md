# Solution
Using the provided PropertyProviderDynamic directly did not converge to useful results, since the class returns, as the name suggests, "random" variables instead of a static one.
How ever we found out that the returned values are all within a certain range and thus decided to average over N samples to get a static result.
For this we created a wrapper class called PropertyProviderDynamicAverage which returns the averages of the PropertyProviderDynamic. This is done in a lazy fashion, meaning the values will only be calculated on first request when needed and then cached for repated calls.
While for the given PropertyProviderDynamic it seems to be a valid solution, this cannot be generalized since if the values given from another PropertyProviderDynamic are not equally distributed or change over time the calculated average is not representive.
Additionally we could also calculate the best- and worst-case scenario instead of the average one.