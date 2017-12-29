
//this class pass all chemicals in a streams that are inert in the system such as nitrogen 

public class InertStream extends Stream
{

  public InertStream(Chemical[]chemicals)
  {
   super(chemicals);
  }
 
  public InertStream(InertStream original)
  {
   super(original);
  }

  public InertStream clone()
  {
   return new InertStream(this);
  }
  

}
