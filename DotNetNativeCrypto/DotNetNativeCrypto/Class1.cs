using System.Runtime.CompilerServices;
using System.Runtime.InteropServices;
using System.Security.Cryptography;

namespace DotNetNativeCrypto;

public static class Class1
{

    [UnmanagedCallersOnly(EntryPoint = "b_hello_world", CallConvs = [typeof(CallConvCdecl)])]
    public static void HelloWorld()
    {
        var msg = "Hello from .NET NativeAOT (DotNetNativeCrypto)!";
        Console.WriteLine(msg);
    }

    [UnmanagedCallersOnly(EntryPoint = "b_hello_world_random", CallConvs = [typeof(CallConvCdecl)])]
    public static void HelloWorldRandom()
    {
        var rnd = RandomNumberGenerator.GetBytes(1);
        var msg = $"Hello from .NET NativeAOT (DotNetNativeCrypto)! ${rnd[0]}";
        Console.WriteLine(msg);
    }


}