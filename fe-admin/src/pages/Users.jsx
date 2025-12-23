import { useState, useEffect } from 'react';
import { userService } from '../services/api';
import { MdPerson, MdBlock } from 'react-icons/md';

const Users = () => {
    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        loadUsers();
    }, []);

    const loadUsers = async () => {
        try {
            const response = await userService.getAll();
            // Backend returns ApiResponse { data: [...], message: "..." }
            const userList = response.data?.data || [];
            const formattedUsers = userList.map(u => ({
                id: u.keycloakUserId || u.email,
                name: `${u.firstName || ''} ${u.lastName || ''}`.trim() || 'Unknown',
                email: u.email,
                role: 'Customer', // Role not provided in DTO
                joined: u.createdAt ? new Date(u.createdAt).toLocaleDateString() : 'N/A'
            }));
            setUsers(formattedUsers);
        } catch (error) {
            console.error("Failed to fetch users", error);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div>
            <h1 className="header-title">User Management</h1>

            <div className="card table-container">
                <table>
                    <thead>
                        <tr>
                            <th>User</th>
                            <th>Email</th>
                            <th>Role</th>
                            <th>Joined Date</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        {loading ? (
                            <tr><td colSpan="5" style={{ textAlign: 'center' }}>Loading...</td></tr>
                        ) : users.map(user => (
                            <tr key={user.id}>
                                <td>
                                    <div style={{ display: 'flex', alignItems: 'center', gap: '0.75rem' }}>
                                        <div style={{
                                            width: '32px',
                                            height: '32px',
                                            borderRadius: '50%',
                                            backgroundColor: 'var(--bg-hover)',
                                            display: 'flex',
                                            alignItems: 'center',
                                            justifyContent: 'center'
                                        }}>
                                            <MdPerson />
                                        </div>
                                        {user.name}
                                    </div>
                                </td>
                                <td>{user.email}</td>
                                <td>
                                    <span style={{
                                        padding: '0.2rem 0.6rem',
                                        borderRadius: '12px',
                                        fontSize: '0.8rem',
                                        backgroundColor: user.role === 'Admin' ? 'var(--primary)' : 'var(--bg-hover)',
                                        color: user.role === 'Admin' ? '#fff' : 'var(--text-secondary)'
                                    }}>
                                        {user.role}
                                    </span>
                                </td>
                                <td>{user.joined}</td>
                                <td>
                                    <button className="btn btn-outline" style={{ padding: '0.4rem', color: 'var(--danger)' }} title="Block User">
                                        <MdBlock size={16} />
                                    </button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default Users;
