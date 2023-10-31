interface Comment { 
    author: string,
    message: string,
    date: string
}

export const comments: Comment[] = [
    {
        author: 'Usuário 1',
        message: 'Fazendo um comentário!',
        date: '15/08/2023'
    },
    {
        author: 'Usuário 2',
        message: 'Fazendo o segundo comentário!',
        date: '18/08/2023'
    }
]