$ErrorActionPreference = "Stop"
try {
    # Try finding the right namespace
    $namespaces = Get-WmiObject -Namespace "root\Microsoft\SqlServer" -Class __NAMESPACE | Select-Object -ExpandProperty Name
    $targetNamespace = $namespaces | Where-Object { $_ -match "ComputerManagement" } | Sort-Object -Descending | Select-Object -First 1

    if (-not $targetNamespace) {
        Write-Host "Could not find SQL Server WMI namespace"
        exit 1
    }

    $namespace = "root\Microsoft\SqlServer\$targetNamespace"
    Write-Host "Using namespace: $namespace"

    # Enable TCP
    $tcp = Get-WmiObject -Namespace $namespace -Class ServerNetworkProtocol -Filter "InstanceName='SQLEXPRESS' and ProtocolName='Tcp'"
    if ($tcp) {
        $tcp.SetEnable()
        Write-Host "TCP Enabled"
    }

    # Configure IPAll to use port 1433
    $props = Get-WmiObject -Namespace $namespace -Class ServerNetworkProtocolProperty -Filter "InstanceName='SQLEXPRESS' and ProtocolName='Tcp' and IPAddressName='IPAll'"
    
    foreach ($p in $props) {
        if ($p.PropertyName -eq 'TcpPort') {
            $p.SetStringValue('1433')
            Write-Host "Set TcpPort to 1433"
        } elseif ($p.PropertyName -eq 'TcpDynamicPorts') {
            $p.SetStringValue('')
            Write-Host "Cleared TcpDynamicPorts"
        }
    }

    # Restart SQL Server Express
    Write-Host "Restarting SQL Server..."
    Restart-Service -Name "MSSQL`$SQLEXPRESS" -Force
    Write-Host "Done!"
    
} catch {
    Write-Host "Error: $_"
    exit 1
}
