<?xml version = "1.0" encoding="UTF-8" standalone="yes"?>
<CPLEXSolution version="1.2">
 <header
   problemName="lpex1.lp"
   objectiveValue="150"
   solutionTypeValue="1"
   solutionTypeString="basic"
   solutionStatusValue="1"
   solutionStatusString="optimal"
   solutionMethodString="dual"
   primalFeasible="1"
   dualFeasible="1"
   simplexIterations="3"
   writeLevel="1"/>
 <quality
   epRHS="1e-06"
   epOpt="1e-06"
   maxPrimalInfeas="0"
   maxDualInfeas="0"
   maxPrimalResidual="0"
   maxDualResidual="0"
   maxX="15"
   maxPi="9"
   maxSlack="0"
   maxRedCost="4"
   kappa="6"/>
 <linearConstraints>
  <constraint name="c1" index="0" status="BS" slack="0" dual="0"/>
  <constraint name="c2" index="1" status="LL" slack="0" dual="4"/>
  <constraint name="c3" index="2" status="LL" slack="0" dual="6"/>
  <constraint name="c4" index="3" status="LL" slack="0" dual="7"/>
  <constraint name="c5" index="4" status="LL" slack="0" dual="9"/>
 </linearConstraints>
 <variables>
  <variable name="x12" index="0" status="BS" value="12" reducedCost="0"/>
  <variable name="x13" index="1" status="UL" value="8" reducedCost="-2"/>
  <variable name="x14" index="2" status="LL" value="0" reducedCost="0"/>
  <variable name="x15" index="3" status="LL" value="0" reducedCost="0"/>
  <variable name="x21" index="4" status="LL" value="0" reducedCost="0"/>
  <variable name="x23" index="5" status="BS" value="8" reducedCost="0"/>
  <variable name="x24" index="6" status="UL" value="4" reducedCost="-1"/>
  <variable name="x25" index="7" status="LL" value="0" reducedCost="1"/>
  <variable name="x31" index="8" status="LL" value="0" reducedCost="0"/>
  <variable name="x32" index="9" status="LL" value="0" reducedCost="0"/>
  <variable name="x34" index="10" status="UL" value="15" reducedCost="0"/>
  <variable name="x35" index="11" status="BS" value="1" reducedCost="0"/>
  <variable name="x41" index="12" status="LL" value="0" reducedCost="0"/>
  <variable name="x42" index="13" status="LL" value="0" reducedCost="0"/>
  <variable name="x43" index="14" status="LL" value="0" reducedCost="0"/>
  <variable name="x45" index="15" status="BS" value="14" reducedCost="0"/>
  <variable name="x51" index="16" status="LL" value="0" reducedCost="0"/>
  <variable name="x52" index="17" status="LL" value="0" reducedCost="0"/>
  <variable name="x53" index="18" status="LL" value="0" reducedCost="4"/>
  <variable name="x54" index="19" status="LL" value="0" reducedCost="0"/>
 </variables>
</CPLEXSolution>
